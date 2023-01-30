package com.prgrms.prolog.global.image.dto;

public record UploadFileResponse(
        String originalFileName,
        String uploadFilePath) {

    public static UploadFileResponse toUploadFileResponse(String originalFileName, String uploadFilePath) {
        return new UploadFileResponse(originalFileName, uploadFilePath);
    }
}
