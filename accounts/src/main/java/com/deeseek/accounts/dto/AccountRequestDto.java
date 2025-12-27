package com.deeseek.accounts.dto;

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
public class AccountRequestDto {

	@NotNull(message = "Customer ID cannot be null")
	private Long customerId;

	@NotBlank(message = "Account type cannot be blank")
	private String accountType;

	@NotBlank(message = "Branch address cannot be blank")
	private String branchAddress;

}

