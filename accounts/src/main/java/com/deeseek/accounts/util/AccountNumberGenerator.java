package com.deeseek.accounts.util;

import com.deeseek.accounts.exception.BusinessException;
import com.deeseek.accounts.exception.ErrorCode;
import com.deeseek.accounts.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

	private static final int MIN_ACCOUNT_NUMBER = 1000000; // 7 digits minimum
	private static final int MAX_ACCOUNT_NUMBER = 9999999; // 7 digits maximum
	private static final int MAX_ATTEMPTS = 100; // Max attempts to generate unique number

	private final AccountRepository accountRepository;
	private final SecureRandom random;

	public AccountNumberGenerator(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
		this.random = new SecureRandom();
	}

	/**
	 * Generates a unique 7-digit account number that is not easy to guess.
	 * Uses SecureRandom to generate non-sequential random numbers.
	 *
	 * @return A unique 7-digit account number
	 * @throws RuntimeException if unable to generate a unique number after max attempts
	 */
	public Long generateAccountNumber() {
		int attempts = 0;
		
		while (attempts < MAX_ATTEMPTS) {
			// Generate random 7-digit number
			Long accountNumber = (long) (MIN_ACCOUNT_NUMBER + random.nextInt(MAX_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER + 1));
			
			// Check if it already exists
			if (!accountRepository.existsByAccountNumber(accountNumber)) {
				return accountNumber;
			}
			
			attempts++;
		}
		
		throw new BusinessException(
				ErrorCode.GENERATION_FAILED,
				"Unable to generate unique account number after " + MAX_ATTEMPTS + " attempts"
		);
	}

}

