package com.prgrms.prolog.global.dto;

public record ErrorResponse(String status, String message) {
	public static ErrorResponse from(String status, String message) {
		return new ErrorResponse(status, message);
	}
}
