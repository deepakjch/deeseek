package com.deeseek.accounts.service

import com.deeseek.accounts.dto.CustomerDto
import com.deeseek.accounts.dto.CustomerRequestDto
import com.deeseek.accounts.entity.Customer
import com.deeseek.accounts.exception.OperationNotAllowedException
import com.deeseek.accounts.exception.ResourceAlreadyExistsException
import com.deeseek.accounts.exception.ResourceNotFoundException
import com.deeseek.accounts.repository.AccountRepository
import com.deeseek.accounts.repository.CustomerRepository
import spock.lang.Specification

import java.time.LocalDate

class CustomerServiceSpec extends Specification {

    CustomerRepository customerRepository = Mock()
    AccountRepository accountRepository = Mock()
    CustomerService customerService = new CustomerService(customerRepository, accountRepository)

    def "should create customer successfully"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "john@example.com",
                    mobileNumber: "81234567"
            )
            def savedCustomer = new Customer(
                    customerId: 1L,
                    name: "John Doe",
                    email: "john@example.com",
                    mobileNumber: "81234567",
                    createdAt: LocalDate.now(),
                    createdBy: "SYSTEM"
            )

        when:
            def result = customerService.createCustomer(requestDto)

        then:
            1 * customerRepository.findByEmail("john@example.com") >> Optional.empty()
            1 * customerRepository.findByMobileNumber("81234567") >> Optional.empty()
            1 * customerRepository.save(_) >> savedCustomer
            1 * accountRepository.findByCustomerId(1L) >> []
            result.customerId == 1L
            result.name == "John Doe"
            result.email == "john@example.com"
    }

    def "should throw exception when email already exists"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "existing@example.com",
                    mobileNumber: "81234567"
            )
            def existingCustomer = new Customer(customerId: 1L, email: "existing@example.com")

        when:
            customerService.createCustomer(requestDto)

        then:
            1 * customerRepository.findByEmail("existing@example.com") >> Optional.of(existingCustomer)
            0 * customerRepository.save(_)
            thrown(ResourceAlreadyExistsException)
    }

    def "should throw exception when mobile number already exists"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "john@example.com",
                    mobileNumber: "81234567"
            )
            def existingCustomer = new Customer(customerId: 1L, mobileNumber: "81234567")

        when:
            customerService.createCustomer(requestDto)

        then:
            1 * customerRepository.findByEmail("john@example.com") >> Optional.empty()
            1 * customerRepository.findByMobileNumber("81234567") >> Optional.of(existingCustomer)
            0 * customerRepository.save(_)
            thrown(ResourceAlreadyExistsException)
    }

    def "should get customer by id successfully"() {
        given:
            def customer = new Customer(
                    customerId: 1L,
                    name: "John Doe",
                    email: "john@example.com",
                    mobileNumber: "81234567"
            )

        when:
            def result = customerService.getCustomer(1L)

        then:
            1 * customerRepository.findById(1L) >> Optional.of(customer)
            1 * accountRepository.findByCustomerId(1L) >> []
            result.customerId == 1L
            result.name == "John Doe"
    }

    def "should throw exception when customer not found"() {
        when:
            customerService.getCustomer(999L)

        then:
            1 * customerRepository.findById(999L) >> Optional.empty()
            thrown(ResourceNotFoundException)
    }

    def "should update customer successfully"() {
        given:
            def existingCustomer = new Customer(
                    customerId: 1L,
                    name: "John Doe",
                    email: "john@example.com",
                    mobileNumber: "81234567"
            )
            def requestDto = new CustomerRequestDto(
                    name: "John Updated",
                    email: "john.updated@example.com",
                    mobileNumber: "98765432"
            )
            def updatedCustomer = new Customer(
                    customerId: 1L,
                    name: "John Updated",
                    email: "john.updated@example.com",
                    mobileNumber: "98765432"
            )

        when:
            def result = customerService.updateCustomer(1L, requestDto)

        then:
            1 * customerRepository.findById(1L) >> Optional.of(existingCustomer)
            1 * customerRepository.findByEmail("john.updated@example.com") >> Optional.empty()
            1 * customerRepository.findByMobileNumber("98765432") >> Optional.empty()
            1 * customerRepository.save(_) >> updatedCustomer
            1 * accountRepository.findByCustomerId(1L) >> []
            result.name == "John Updated"
    }

    def "should delete customer successfully"() {
        given:
            def customer = new Customer(customerId: 1L)

        when:
            customerService.deleteCustomer(1L)

        then:
            1 * customerRepository.findById(1L) >> Optional.of(customer)
            1 * accountRepository.findByCustomerId(1L) >> []
            1 * customerRepository.delete(customer)
    }

    def "should throw exception when deleting customer with accounts"() {
        given:
            def customer = new Customer(customerId: 1L)
            def account = new com.deeseek.accounts.entity.Account(accountNumber: 1234567L)

        when:
            customerService.deleteCustomer(1L)

        then:
            1 * customerRepository.findById(1L) >> Optional.of(customer)
            1 * accountRepository.findByCustomerId(1L) >> [account]
            0 * customerRepository.delete(_)
            thrown(OperationNotAllowedException)
    }
}

