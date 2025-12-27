package com.deeseek.accounts.dto;

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
public class ErrorResponseDto {

	private String apiPath;
	private String errorCode;
	private String errorMessage;
	private LocalDateTime errorTime;
	private Map<String, String> errorDetails;
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

