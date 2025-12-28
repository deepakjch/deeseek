package com.deeseek.accounts.service;

import com.deeseek.accounts.dto.AccountDto;
import com.deeseek.accounts.dto.AccountRequestDto;
import com.deeseek.accounts.entity.Account;
import com.deeseek.accounts.exception.ResourceNotFoundException;
import com.deeseek.accounts.repository.AccountRepository;
import com.deeseek.accounts.repository.CustomerRepository;
import com.deeseek.accounts.util.AccountNumberGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AccountService {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final AccountNumberGenerator accountNumberGenerator;

	public AccountDto createAccount(AccountRequestDto requestDto) {
		if (requestDto == null) {
			throw new IllegalArgumentException("Account request cannot be null");
		}
		Long customerId = Objects.requireNonNull(requestDto.getCustomerId(), "Customer ID cannot be null");
		// Verify customer exists
		if (!customerRepository.existsById(customerId)) {
			throw new ResourceNotFoundException("Customer not found with id: " + customerId);
		}

		Long accountNumber = Objects.requireNonNull(
				accountNumberGenerator.generateAccountNumber(),
				"Failed to generate account number"
		);

		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setCustomerId(requestDto.getCustomerId());
		account.setAccountType(requestDto.getAccountType());
		account.setBranchAddress(requestDto.getBranchAddress());
		// Audit fields (createdAt, createdBy, updatedAt, updatedBy) are automatically set by JPA auditing

		Account savedAccount = accountRepository.save(account);
		return mapToDto(savedAccount);
	}

	public AccountDto getAccount(Long accountNumber) {
		if (accountNumber == null) {
			throw new IllegalArgumentException("Account number cannot be null");
		}
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));
		return mapToDto(account);
	}

	public List<AccountDto> getAllAccounts() {
		return accountRepository.findAll().stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public List<AccountDto> getAccountsByCustomerId(Long customerId) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null");
		}
		if (!customerRepository.existsById(customerId)) {
			throw new ResourceNotFoundException("Customer not found with id: " + customerId);
		}
		return accountRepository.findByCustomerId(customerId).stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public AccountDto updateAccount(Long accountNumber, AccountRequestDto requestDto) {
		if (accountNumber == null) {
			throw new IllegalArgumentException("Account number cannot be null");
		}
		if (requestDto == null) {
			throw new IllegalArgumentException("Account request cannot be null");
		}
		Long customerId = Objects.requireNonNull(requestDto.getCustomerId(), "Customer ID cannot be null");
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));

		// Verify customer exists if customerId is being changed
		if (!account.getCustomerId().equals(customerId)) {
			if (!customerRepository.existsById(customerId)) {
				throw new RuntimeException("Customer not found with id: " + customerId);
			}
		}

		account.setCustomerId(requestDto.getCustomerId());
		account.setAccountType(requestDto.getAccountType());
		account.setBranchAddress(requestDto.getBranchAddress());
		// Audit fields (updatedAt, updatedBy) are automatically set by JPA auditing

		Account updatedAccount = accountRepository.save(account);
		return mapToDto(updatedAccount);
	}

	public AccountDto partialUpdateAccount(Long accountNumber, AccountRequestDto requestDto) {
		if (accountNumber == null) {
			throw new IllegalArgumentException("Account number cannot be null");
		}
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));

		if (requestDto != null) {
			if (requestDto.getCustomerId() != null) {
				Long customerId = Objects.requireNonNull(requestDto.getCustomerId(), "Customer ID cannot be null");
				if (!account.getCustomerId().equals(customerId)) {
					if (!customerRepository.existsById(customerId)) {
						throw new RuntimeException("Customer not found with id: " + customerId);
					}
					account.setCustomerId(customerId);
				}
			}
			if (requestDto.getAccountType() != null) {
				account.setAccountType(requestDto.getAccountType());
			}
			if (requestDto.getBranchAddress() != null) {
				account.setBranchAddress(requestDto.getBranchAddress());
			}
		}
		// Audit fields (updatedAt, updatedBy) are automatically set by JPA auditing

		Account updatedAccount = accountRepository.save(account);
		return mapToDto(updatedAccount);
	}

	public void deleteAccount(Long accountNumber) {
		if (accountNumber == null) {
			throw new IllegalArgumentException("Account number cannot be null");
		}
		Account account = Objects.requireNonNull(
				accountRepository.findByAccountNumber(accountNumber)
						.orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber)),
				"Account cannot be null"
		);

		accountRepository.delete(account);
	}

	private AccountDto mapToDto(Account account) {
		Account nonNullAccount = Objects.requireNonNull(account, "Account cannot be null");
		return AccountDto.builder()
				.accountNumber(nonNullAccount.getAccountNumber())
				.customerId(nonNullAccount.getCustomerId())
				.accountType(nonNullAccount.getAccountType())
				.branchAddress(nonNullAccount.getBranchAddress())
				.createdAt(nonNullAccount.getCreatedAt())
				.createdBy(nonNullAccount.getCreatedBy())
				.updatedAt(nonNullAccount.getUpdatedAt())
				.updatedBy(nonNullAccount.getUpdatedBy())
				.build();
	}

}

