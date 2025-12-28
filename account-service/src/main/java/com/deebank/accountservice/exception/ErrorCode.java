package com.deebank.accountservice.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed for the request"),
	CONSTRAINT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Constraint validation failed"),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid input provided"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter provided"),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
	RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Resource already exists"),
	OPERATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Operation not allowed"),
	GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Generation operation failed"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

	private final HttpStatus httpStatus;
	private final String defaultMessage;

	ErrorCode(HttpStatus httpStatus, String defaultMessage) {
		this.httpStatus = httpStatus;
		this.defaultMessage = defaultMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public String getCode() {
		return this.name();
	}

}

