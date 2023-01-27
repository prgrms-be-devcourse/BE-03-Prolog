package com.prgrms.prolog.global.image.api;

import com.prgrms.prolog.global.image.dto.UploadFileResponse;
import com.prgrms.prolog.global.image.util.FileManager;
import com.prgrms.prolog.global.image.util.UploadFileToS3;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final FileManager fileManager;
    private final UploadFileToS3 uploadFileToS3;

    private static final String FILE_PATH = "posts";

    @PostMapping("/file")
    public UploadFileResponse uploadFile(
            @RequestPart(value = "file") MultipartFile multipartFile
    ) {
        String id = UUID.randomUUID().toString();
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = (originalFilename + id).replace(" ", "");

        File tempTargetFile = fileManager.convertMultipartFileToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("exception.file.convert"));
        String fileUrl = uploadFileToS3.upload(tempTargetFile, FILE_PATH, fileName);

        fileManager.removeFile(tempTargetFile);
        return UploadFileResponse.toUploadFileResponse(originalFilename, fileUrl);
    }
}
