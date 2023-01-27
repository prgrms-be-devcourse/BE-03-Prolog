package com.prgrms.prolog.global.image.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class UploadFileToS3 {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public String upload(File uploadFile, String dirName, String fileName) {
        String savedFileName = dirName + "/" + fileName;
        amazonS3Client.putObject(new PutObjectRequest(bucket, savedFileName, uploadFile));
        return amazonS3Client.getUrl(bucket, savedFileName).toString();
    }
}
