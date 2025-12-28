package com.deebank.accountservice.dto;

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
public class CustomerDto {

	private Long customerId;
	private String name;
	private String email;
	private String mobileNumber;
	private List<Long> accountNumbers;
	private LocalDate createdAt;
	private String createdBy;
	private LocalDate updatedAt;
	private String updatedBy;

}

