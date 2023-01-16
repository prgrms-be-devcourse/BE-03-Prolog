package com.prgrms.prolog.global.exception;

import static org.springframework.http.HttpStatus.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.prgrms.prolog.global.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse handleMethodArgumentNotValidException(HttpServletRequest request, BindException e) {
		logDebug(request, e);
		log.debug("[EXCEPTION] FIELD_ERROR       -----> [{}]", e.getFieldError());
		return ErrorResponse.of(BAD_REQUEST.name(), "잘못된 데이터입니다.");
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MissingRequestValueException.class)
	public ErrorResponse handleMissingRequestValueException(HttpServletRequest request,
		MissingRequestValueException e) {
		logDebug(request, e);
		return ErrorResponse.of(BAD_REQUEST.name(), "데이터가 충분하지 않습니다.");
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
		HttpRequestMethodNotSupportedException e) {
		logDebug(request, e);
		return ErrorResponse.of(BAD_REQUEST.name(), "잘못된 요청입니다.");
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(NoHandlerFoundException.class)
	public ErrorResponse handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
		logDebug(request, e);
		return ErrorResponse.of(BAD_REQUEST.name(), "잘못된 uri 입니다.");
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(EntityNotFoundException.class)
	public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
		logWarn(e);
		return ErrorResponse.of(BAD_REQUEST.name(), e.getMessage());
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
		logWarn(e);
		return ErrorResponse.of(BAD_REQUEST.name(), e.getMessage());
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(IllegalStateException.class)
	public ErrorResponse handleIllegalStateException(IllegalStateException e) {
		logWarn(e);
		return ErrorResponse.of(BAD_REQUEST.name(), e.getMessage());
	}

	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleException(Exception e) {
		logError(e);
		return ErrorResponse.of(INTERNAL_SERVER_ERROR.name(), "서버 내부에서 에러가 발생하였습니다.");
	}

	private void logDebug(HttpServletRequest request, Exception e) {
		log.debug("[EXCEPTION] REQUEST_URI       -----> [{}]", request.getRequestURI());
		log.debug("[EXCEPTION] HTTP_METHOD_TYPE  -----> [{}]", request.getMethod());
		log.debug("[EXCEPTION] EXCEPTION_TYPE    -----> [{}]", e.getClass().getSimpleName());
		log.debug("[EXCEPTION] EXCEPTION_MESSAGE -----> [{}]", e.getMessage());
		log.debug("[EXCEPTION]                   -----> ", e);
	}

	private void logWarn(Exception e) {
		log.warn("[EXCEPTION] EXCEPTION_TYPE    -----> [{}]", e.getClass().getSimpleName());
		log.warn("[EXCEPTION] EXCEPTION_MESSAGE -----> [{}]", e.getMessage());
		log.warn("[EXCEPTION]                   -----> ", e);
	}

	private void logError(Exception e) {
		log.error("[EXCEPTION] EXCEPTION_TYPE    -----> [{}]", e.getClass().getSimpleName());
		log.error("[EXCEPTION] EXCEPTION_MESSAGE -----> [{}]", e.getMessage());
		log.error("[EXCEPTION]                   -----> ", e);
	}
}
