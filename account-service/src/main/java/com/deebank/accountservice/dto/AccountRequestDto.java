package com.deebank.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for creating or updating an account")
public class AccountRequestDto {

	@NotNull(message = "Customer ID cannot be null")
	@Schema(description = "Customer ID who will own this account", required = true, example = "1")
	private Long customerId;

	@NotBlank(message = "Account type cannot be blank")
	@Schema(description = "Type of account", required = true, example = "Savings", allowableValues = {"Savings", "Checking", "Current"})
	private String accountType;

	@NotBlank(message = "Branch address cannot be blank")
	@Schema(description = "Branch address where the account will be held", required = true, example = "123 Main Street, Singapore 123456")
	private String branchAddress;

}

