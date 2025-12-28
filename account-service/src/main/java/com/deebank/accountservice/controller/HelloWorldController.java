package com.deebank.accountservice.controller;

import com.deebank.accountservice.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "Simple health check endpoint")
public class HelloWorldController {

	@GetMapping("/hello")
	@Operation(
			summary = "Hello World endpoint",
			description = "Simple endpoint to verify the service is running"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Service is running"
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public String sayHello() {
		return "Hello, World!";
	}
}

