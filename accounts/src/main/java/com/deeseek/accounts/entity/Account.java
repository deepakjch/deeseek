package com.deeseek.accounts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

	@Id
	@Column(name = "account_number")
	private Long accountNumber;

	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	@Column(name = "account_type", nullable = false, length = 100)
	private String accountType;

	@Column(name = "branch_address", nullable = false, length = 200)
	private String branchAddress;

}

