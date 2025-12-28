package com.deebank.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Standard API response wrapper")
public class ResponseDto<T> {

	@Schema(description = "HTTP status code", example = "200")
	private String statusCode;

	@Schema(description = "Status message", example = "Success")
	private String statusMsg;

	@Schema(description = "Response data payload")
	private T data;

	@Schema(description = "Timestamp of the response", example = "2024-01-15T10:30:00")
	private LocalDateTime responseTime;

	public static <T> ResponseDto<T> success(T data) {
		return ResponseDto.<T>builder()
				.statusCode("200")
				.statusMsg("Success")
				.data(data)
				.responseTime(LocalDateTime.now())
				.build();
	}

	public static <T> ResponseDto<T> success(String message, T data) {
		return ResponseDto.<T>builder()
				.statusCode("200")
				.statusMsg(message)
				.data(data)
				.responseTime(LocalDateTime.now())
				.build();
	}

	public static <T> ResponseDto<T> error(String statusCode, String message) {
		return ResponseDto.<T>builder()
				.statusCode(statusCode)
				.statusMsg(message)
				.data(null)
				.responseTime(LocalDateTime.now())
				.build();
	}

}

