package com.deebank.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Customer information response")
public class CustomerDto {

	@Schema(description = "Unique customer ID", example = "1")
	private Long customerId;

	@Schema(description = "Full name of the customer", example = "John Doe")
	private String name;

	@Schema(description = "Email address of the customer", example = "john.doe@example.com")
	private String email;

	@Schema(description = "Mobile number of the customer (8 digits starting with 8 or 9)", example = "81234567")
	private String mobileNumber;

	@Schema(description = "List of account numbers associated with this customer", example = "[1234567, 1234568]")
	private List<Long> accountNumbers;

	@Schema(description = "Date when the customer was created", example = "2024-01-15")
	private LocalDate createdAt;

	@Schema(description = "User who created the customer", example = "admin")
	private String createdBy;

	@Schema(description = "Date when the customer was last updated", example = "2024-01-20")
	private LocalDate updatedAt;

	@Schema(description = "User who last updated the customer", example = "admin")
	private String updatedBy;

}

