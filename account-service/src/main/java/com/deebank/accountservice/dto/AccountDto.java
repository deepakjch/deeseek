package com.deebank.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Account information response")
public class AccountDto {

	@Schema(description = "Unique account number", example = "1234567")
	private Long accountNumber;

	@Schema(description = "Customer ID who owns this account", example = "1")
	private Long customerId;

	@Schema(description = "Type of account", example = "Savings", allowableValues = {"Savings", "Checking", "Current"})
	private String accountType;

	@Schema(description = "Branch address where the account is held", example = "123 Main Street, Singapore 123456")
	private String branchAddress;

	@Schema(description = "Date when the account was created", example = "2024-01-15")
	private LocalDate createdAt;

	@Schema(description = "User who created the account", example = "admin")
	private String createdBy;

	@Schema(description = "Date when the account was last updated", example = "2024-01-20")
	private LocalDate updatedAt;

	@Schema(description = "User who last updated the account", example = "admin")
	private String updatedBy;

}

