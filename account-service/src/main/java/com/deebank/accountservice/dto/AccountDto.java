package com.deebank.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

	private Long accountNumber;
	private Long customerId;
	private String accountType;
	private String branchAddress;
	private LocalDate createdAt;
	private String createdBy;
	private LocalDate updatedAt;
	private String updatedBy;

}

