package com.prgrms.prolog.domain.post.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class IllegalAccessPostException extends RuntimeException {

	private final HttpStatus status;
	private final String message;

	public IllegalAccessPostException(String message) {
		this.status = FORBIDDEN;
		this.message = message;
	}

}