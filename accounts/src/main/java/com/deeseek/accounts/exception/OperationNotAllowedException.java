package com.deeseek.accounts.exception;

public class OperationNotAllowedException extends RuntimeException {

	private final ErrorCode errorCode;

	public OperationNotAllowedException(String message) {
		super(message);
		this.errorCode = ErrorCode.OPERATION_NOT_ALLOWED;
	}

	public OperationNotAllowedException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = ErrorCode.OPERATION_NOT_ALLOWED;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}

