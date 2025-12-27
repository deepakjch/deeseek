package com.deeseek.accounts.exception;

import com.deeseek.accounts.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
			MethodArgumentNotValidException ex, WebRequest request) {
		
		List<String> errors = new ArrayList<>();
		Map<String, String> errorDetails = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorDetails.put(fieldName, errorMessage);
			errors.add(fieldName + ": " + errorMessage);
		});

		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				ErrorCode.VALIDATION_FAILED.getCode(),
				ErrorCode.VALIDATION_FAILED.getDefaultMessage(),
				errors
		);
		errorResponse.setErrorDetails(errorDetails);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(ErrorCode.VALIDATION_FAILED.getHttpStatus()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
			ConstraintViolationException ex, WebRequest request) {
		
		List<String> errors = ex.getConstraintViolations()
				.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());

		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				ErrorCode.CONSTRAINT_VALIDATION_FAILED.getCode(),
				ErrorCode.CONSTRAINT_VALIDATION_FAILED.getDefaultMessage(),
				errors
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(ErrorCode.CONSTRAINT_VALIDATION_FAILED.getHttpStatus()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
			IllegalArgumentException ex, WebRequest request) {
		
		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				ErrorCode.INVALID_INPUT.getCode(),
				ex.getMessage() != null ? ex.getMessage() : ErrorCode.INVALID_INPUT.getDefaultMessage()
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(ErrorCode.INVALID_INPUT.getHttpStatus()));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
			MethodArgumentTypeMismatchException ex, WebRequest request) {
		
		Class<?> requiredType = ex.getRequiredType();
		String typeName = requiredType != null ? requiredType.getSimpleName() : "unknown";
		String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
				ex.getValue(),
				ex.getName(),
				typeName);

		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				ErrorCode.INVALID_PARAMETER.getCode(),
				message
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(ErrorCode.INVALID_PARAMETER.getHttpStatus()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
			ResourceNotFoundException ex, WebRequest request) {
		
		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorCode errorCode = ex.getErrorCode();
		
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				errorCode.getCode(),
				ex.getMessage() != null ? ex.getMessage() : errorCode.getDefaultMessage()
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(errorCode.getHttpStatus()));
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistsException(
			ResourceAlreadyExistsException ex, WebRequest request) {
		
		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorCode errorCode = ex.getErrorCode();
		
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				errorCode.getCode(),
				ex.getMessage() != null ? ex.getMessage() : errorCode.getDefaultMessage()
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(errorCode.getHttpStatus()));
	}

	@ExceptionHandler(OperationNotAllowedException.class)
	public ResponseEntity<ErrorResponseDto> handleOperationNotAllowedException(
			OperationNotAllowedException ex, WebRequest request) {
		
		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorCode errorCode = ex.getErrorCode();
		
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				errorCode.getCode(),
				ex.getMessage() != null ? ex.getMessage() : errorCode.getDefaultMessage()
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(errorCode.getHttpStatus()));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponseDto> handleBusinessException(
			BusinessException ex, WebRequest request) {
		
		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorCode errorCode = ex.getErrorCode();
		
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				errorCode.getCode(),
				ex.getMessage() != null ? ex.getMessage() : errorCode.getDefaultMessage()
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(errorCode.getHttpStatus()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponseDto> handleRuntimeException(
			RuntimeException ex, WebRequest request) {
		
		String apiPath = request.getDescription(false).replace("uri=", "");
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				errorCode.getCode(),
				ex.getMessage() != null ? ex.getMessage() : errorCode.getDefaultMessage()
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(errorCode.getHttpStatus()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleGenericException(
			Exception ex, WebRequest request) {
		
		String apiPath = request.getDescription(false).replace("uri=", "");
		String errorMessage = ex.getMessage() != null 
				? ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage() + ": " + ex.getMessage()
				: ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage();
		
		ErrorResponseDto errorResponse = ErrorResponseDto.of(
				apiPath,
				ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
				errorMessage
		);

		return new ResponseEntity<>(errorResponse, Objects.requireNonNull(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()));
	}

}

