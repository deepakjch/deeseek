package com.deebank.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Error response containing error details")
public class ErrorResponseDto {

	@Schema(description = "API path where the error occurred", example = "/api/accounts/1234567")
	private String apiPath;

	@Schema(description = "Error code", example = "ACCOUNT_NOT_FOUND")
	private String errorCode;

	@Schema(description = "Error message", example = "Account with number 1234567 not found")
	private String errorMessage;

	@Schema(description = "Timestamp when the error occurred", example = "2024-01-15T10:30:00")
	private LocalDateTime errorTime;

	@Schema(description = "Additional error details as key-value pairs")
	private Map<String, String> errorDetails;

	@Schema(description = "List of validation errors")
	private List<String> validationErrors;

	public static ErrorResponseDto of(String apiPath, String errorCode, String errorMessage) {
		return ErrorResponseDto.builder()
				.apiPath(apiPath)
				.errorCode(errorCode)
				.errorMessage(errorMessage)
				.errorTime(LocalDateTime.now())
				.build();
	}

	public static ErrorResponseDto of(String apiPath, String errorCode, String errorMessage, List<String> validationErrors) {
		return ErrorResponseDto.builder()
				.apiPath(apiPath)
				.errorCode(errorCode)
				.errorMessage(errorMessage)
				.errorTime(LocalDateTime.now())
				.validationErrors(validationErrors)
				.build();
	}

	public static ErrorResponseDto of(String apiPath, String errorCode, String errorMessage, Map<String, String> errorDetails) {
		return ErrorResponseDto.builder()
				.apiPath(apiPath)
				.errorCode(errorCode)
				.errorMessage(errorMessage)
				.errorTime(LocalDateTime.now())
				.errorDetails(errorDetails)
				.build();
	}

}

