package com.deeseek.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

	private String statusCode;
	private String statusMsg;
	private T data;
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

