package com.deeseek.accounts.service

import com.deeseek.accounts.dto.AccountDto
import com.deeseek.accounts.dto.AccountRequestDto
import com.deeseek.accounts.entity.Account
import com.deeseek.accounts.exception.ResourceNotFoundException
import com.deeseek.accounts.repository.AccountRepository
import com.deeseek.accounts.repository.CustomerRepository
import com.deeseek.accounts.util.AccountNumberGenerator
import spock.lang.Specification

import java.time.LocalDate

class AccountServiceTest extends Specification {

    AccountRepository accountRepository = Mock()
    CustomerRepository customerRepository = Mock()
    AccountNumberGenerator accountNumberGenerator = Mock()
    AccountService accountService = new AccountService(accountRepository, customerRepository, accountNumberGenerator)

    def "should create account successfully"() {
        given:
            def requestDto = new AccountRequestDto(
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: "123 Main St"
            )
            def savedAccount = new Account(
                    accountNumber: 1234567L,
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: "123 Main St",
                    createdAt: LocalDate.now(),
                    createdBy: "SYSTEM"
            )

        when:
            def result = accountService.createAccount(requestDto, "SYSTEM")

        then:
            1 * customerRepository.existsById(1L) >> true
            1 * accountNumberGenerator.generateAccountNumber() >> 1234567L
            1 * accountRepository.save(_) >> savedAccount
            result.accountNumber == 1234567L
            result.accountType == "Savings"
    }

    def "should throw exception when customer does not exist"() {
        given:
            def requestDto = new AccountRequestDto(
                    customerId: 999L,
                    accountType: "Savings",
                    branchAddress: "123 Main St"
            )

        when:
            accountService.createAccount(requestDto, "SYSTEM")

        then:
            1 * customerRepository.existsById(999L) >> false
            0 * accountRepository.save(_)
            thrown(ResourceNotFoundException)
    }

    def "should get account by account number successfully"() {
        given:
            def account = new Account(
                    accountNumber: 1234567L,
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: "123 Main St"
            )

        when:
            def result = accountService.getAccount(1234567L)

        then:
            1 * accountRepository.findByAccountNumber(1234567L) >> Optional.of(account)
            result.accountNumber == 1234567L
            result.accountType == "Savings"
    }

    def "should throw exception when account not found"() {
        when:
            accountService.getAccount(9999999L)

        then:
            1 * accountRepository.findByAccountNumber(9999999L) >> Optional.empty()
            thrown(ResourceNotFoundException)
    }

    def "should update account successfully"() {
        given:
            def existingAccount = new Account(
                    accountNumber: 1234567L,
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: "123 Main St"
            )
            def requestDto = new AccountRequestDto(
                    customerId: 2L,  // Different customer ID to trigger the check
                    accountType: "Checking",
                    branchAddress: "456 Oak Ave"
            )
            def updatedAccount = new Account(
                    accountNumber: 1234567L,
                    customerId: 2L,
                    accountType: "Checking",
                    branchAddress: "456 Oak Ave"
            )

        when:
            def result = accountService.updateAccount(1234567L, requestDto, "SYSTEM")

        then:
            1 * accountRepository.findByAccountNumber(1234567L) >> Optional.of(existingAccount)
            1 * customerRepository.existsById(2L) >> true
            1 * accountRepository.save(_) >> updatedAccount
            result.accountType == "Checking"
    }

    def "should partially update account"() {
        given:
            def existingAccount = new Account(
                    accountNumber: 1234567L,
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: "123 Main St"
            )
            def requestDto = new AccountRequestDto(
                    customerId: null,
                    accountType: "Investment",
                    branchAddress: null
            )
            def updatedAccount = new Account(
                    accountNumber: 1234567L,
                    accountType: "Investment"
            )

        when:
            def result = accountService.partialUpdateAccount(1234567L, requestDto, "SYSTEM")

        then:
            1 * accountRepository.findByAccountNumber(1234567L) >> Optional.of(existingAccount)
            1 * accountRepository.save(_) >> updatedAccount
            result.accountType == "Investment"
    }

    def "should delete account successfully"() {
        given:
            def account = new Account(accountNumber: 1234567L)

        when:
            accountService.deleteAccount(1234567L)

        then:
            1 * accountRepository.findByAccountNumber(1234567L) >> Optional.of(account)
            1 * accountRepository.delete(account)
    }
}

