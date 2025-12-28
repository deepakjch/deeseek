package com.deeseek.accounts.integration

import com.deeseek.accounts.dto.AccountDto
import com.deeseek.accounts.dto.AccountRequestDto
import com.deeseek.accounts.dto.CustomerRequestDto
import com.deeseek.accounts.exception.ResourceNotFoundException
import com.deeseek.accounts.service.AccountService
import com.deeseek.accounts.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration(classes = [com.deeseek.accounts.AccountsApplication])
@ActiveProfiles("test")
@Transactional
class AccountServiceIntSpec extends Specification {

    @Autowired
    AccountService accountService

    @Autowired
    CustomerService customerService

    def "should create account with auto-generated account number"() {
        given:
            def customerRequest = new CustomerRequestDto(
                    name: "Test Customer",
                    email: "customer@test.com",
                    mobileNumber: "81234567"
            )
            def customer = customerService.createCustomer(customerRequest)

            def accountRequest = new AccountRequestDto(
                    customerId: customer.customerId,
                    accountType: "Savings",
                    branchAddress: "123 Test Street"
            )

        when:
            def account = accountService.createAccount(accountRequest)

        then:
            account.accountNumber != null
            account.accountNumber >= 1000000L
            account.accountNumber <= 9999999L
            account.customerId == customer.customerId
            account.accountType == "Savings"
            account.branchAddress == "123 Test Street"
            account.createdBy == "Account Service"
    }

    def "should throw exception when creating account for non-existent customer"() {
        given:
            def accountRequest = new AccountRequestDto(
                    customerId: 99999L,
                    accountType: "Savings",
                    branchAddress: "123 Test Street"
            )

        when:
            accountService.createAccount(accountRequest)

        then:
            thrown(ResourceNotFoundException)
    }

    def "should retrieve accounts by customer id"() {
        given:
            def customerRequest = new CustomerRequestDto(
                    name: "Test Customer",
                    email: "customer2@test.com",
                    mobileNumber: "87654321"
            )
            def customer = customerService.createCustomer(customerRequest)

            def accountRequest1 = new AccountRequestDto(
                    customerId: customer.customerId,
                    accountType: "Savings",
                    branchAddress: "123 Test Street"
            )
            def accountRequest2 = new AccountRequestDto(
                    customerId: customer.customerId,
                    accountType: "Checking",
                    branchAddress: "456 Test Avenue"
            )
            accountService.createAccount(accountRequest1)
            accountService.createAccount(accountRequest2)

        when:
            def accounts = accountService.getAccountsByCustomerId(customer.customerId)

        then:
            accounts.size() == 2
            accounts.any { it.accountType == "Savings" }
            accounts.any { it.accountType == "Checking" }
    }

    def "should update account successfully"() {
        given:
            def customerRequest = new CustomerRequestDto(
                    name: "Test Customer",
                    email: "customer3@test.com",
                    mobileNumber: "81111111"
            )
            def customer = customerService.createCustomer(customerRequest)

            def accountRequest = new AccountRequestDto(
                    customerId: customer.customerId,
                    accountType: "Savings",
                    branchAddress: "123 Test Street"
            )
            def createdAccount = accountService.createAccount(accountRequest)

            def updateRequest = new AccountRequestDto(
                    customerId: customer.customerId,
                    accountType: "Checking",
                    branchAddress: "456 Updated Street"
            )

        when:
            def updated = accountService.updateAccount(createdAccount.accountNumber, updateRequest)

        then:
            updated.accountType == "Checking"
            updated.branchAddress == "456 Updated Street"
            updated.updatedBy == "Account Service"
    }

    def "should delete account successfully"() {
        given:
            def customerRequest = new CustomerRequestDto(
                    name: "Test Customer",
                    email: "customer4@test.com",
                    mobileNumber: "82222222"
            )
            def customer = customerService.createCustomer(customerRequest)

            def accountRequest = new AccountRequestDto(
                    customerId: customer.customerId,
                    accountType: "Savings",
                    branchAddress: "123 Test Street"
            )
            def account = accountService.createAccount(accountRequest)

        when:
            accountService.deleteAccount(account.accountNumber)
            accountService.getAccount(account.accountNumber)

        then:
            thrown(ResourceNotFoundException)
    }
}

