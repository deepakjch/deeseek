package com.deebank.accountservice.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

	private final ErrorCode errorCode;

	public ResourceAlreadyExistsException(String message) {
		super(message);
		this.errorCode = ErrorCode.RESOURCE_ALREADY_EXISTS;
	}

	public ResourceAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = ErrorCode.RESOURCE_ALREADY_EXISTS;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}

