package com.deebank.accountservice.exception;

public class ResourceNotFoundException extends RuntimeException {

	private final ErrorCode errorCode;

	public ResourceNotFoundException(String message) {
		super(message);
		this.errorCode = ErrorCode.RESOURCE_NOT_FOUND;
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = ErrorCode.RESOURCE_NOT_FOUND;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}

