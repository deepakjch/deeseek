package com.deebank.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for creating or updating a customer")
public class CustomerRequestDto {

	@NotBlank(message = "Name cannot be blank")
	@Schema(description = "Full name of the customer", required = true, example = "John Doe")
	private String name;

	@NotBlank(message = "Email cannot be blank")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email should be valid")
	@Schema(description = "Email address of the customer (must be unique)", required = true, example = "john.doe@example.com")
	private String email;

	@NotBlank(message = "Mobile number cannot be blank")
	@Pattern(regexp = "^[89][0-9]{7}$", message = "Mobile number should be 8 digits starting with 8 or 9")
	@Schema(description = "Mobile number of the customer (8 digits starting with 8 or 9, must be unique)", required = true, example = "81234567")
	private String mobileNumber;

}

