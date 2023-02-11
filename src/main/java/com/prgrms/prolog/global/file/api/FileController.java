package com.prgrms.prolog.global.file.api;

import static com.prgrms.prolog.global.config.MessageKeyConfig.*;

import java.io.File;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.prolog.global.file.dto.UploadFileResponse;
import com.prgrms.prolog.global.file.util.FileManager;
import com.prgrms.prolog.global.file.util.UploadFileToS3;

import lombok.RequiredArgsConstructor;

@Profile("!test")
@RestController
@RequiredArgsConstructor
public class FileController {

	private static final String FILE_PATH = "posts";
	private final FileManager fileManager;
	private final UploadFileToS3 uploadFileToS3;

	@PostMapping("/file")
	public UploadFileResponse uploadFile(
		@RequestPart(value = "file") MultipartFile multipartFile
	) {
		String id = UUID.randomUUID().toString();
		String originalFilename = multipartFile.getOriginalFilename();
		String fileName = (originalFilename + id).replace(" ", "");

		File tempTargetFile = fileManager.convertMultipartFileToFile(multipartFile)
			.orElseThrow(() -> new IllegalArgumentException(messageKey().exception().file().convert().endKey()));
		String fileUrl = uploadFileToS3.upload(tempTargetFile, FILE_PATH, fileName);

		fileManager.removeFile(tempTargetFile);
		return UploadFileResponse.toUploadFileResponse(originalFilename, fileUrl);
	}
}
