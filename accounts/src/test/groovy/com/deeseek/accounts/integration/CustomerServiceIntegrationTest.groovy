package com.deeseek.accounts.integration

import com.deeseek.accounts.dto.CustomerDto
import com.deeseek.accounts.dto.CustomerRequestDto
import com.deeseek.accounts.exception.OperationNotAllowedException
import com.deeseek.accounts.exception.ResourceAlreadyExistsException
import com.deeseek.accounts.exception.ResourceNotFoundException
import com.deeseek.accounts.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CustomerServiceIntegrationTest extends Specification {

    @Autowired
    CustomerService customerService

    def "should create and retrieve customer end-to-end"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "Integration Test Customer",
                    email: "integration@test.com",
                    mobileNumber: "81234567"
            )

        when:
            def created = customerService.createCustomer(requestDto, "TEST_USER")

        then:
            created.customerId != null
            created.name == "Integration Test Customer"
            created.email == "integration@test.com"
            created.mobileNumber == "81234567"
            created.createdBy == "TEST_USER"

        when:
            def retrieved = customerService.getCustomer(created.customerId)

        then:
            retrieved.customerId == created.customerId
            retrieved.name == created.name
            retrieved.email == created.email
    }

    def "should prevent duplicate email on create"() {
        given:
            def requestDto1 = new CustomerRequestDto(
                    name: "First Customer",
                    email: "duplicate@test.com",
                    mobileNumber: "81234567"
            )
            def requestDto2 = new CustomerRequestDto(
                    name: "Second Customer",
                    email: "duplicate@test.com",
                    mobileNumber: "98765432"
            )

        when:
            customerService.createCustomer(requestDto1, "TEST_USER")
            customerService.createCustomer(requestDto2, "TEST_USER")

        then:
            thrown(ResourceAlreadyExistsException)
    }

    def "should prevent duplicate mobile number on create"() {
        given:
            def requestDto1 = new CustomerRequestDto(
                    name: "First Customer",
                    email: "first@test.com",
                    mobileNumber: "81234567"
            )
            def requestDto2 = new CustomerRequestDto(
                    name: "Second Customer",
                    email: "second@test.com",
                    mobileNumber: "81234567"
            )

        when:
            customerService.createCustomer(requestDto1, "TEST_USER")
            customerService.createCustomer(requestDto2, "TEST_USER")

        then:
            thrown(ResourceAlreadyExistsException)
    }

    def "should update customer successfully"() {
        given:
            def createRequest = new CustomerRequestDto(
                    name: "Original Name",
                    email: "original@test.com",
                    mobileNumber: "81234567"
            )
            def created = customerService.createCustomer(createRequest, "TEST_USER")
            
            def updateRequest = new CustomerRequestDto(
                    name: "Updated Name",
                    email: "updated@test.com",
                    mobileNumber: "98765432"
            )

        when:
            def updated = customerService.updateCustomer(created.customerId, updateRequest, "TEST_USER")

        then:
            updated.name == "Updated Name"
            updated.email == "updated@test.com"
            updated.mobileNumber == "98765432"
            updated.updatedBy == "TEST_USER"
    }

    def "should throw exception when customer not found for update"() {
        given:
            def updateRequest = new CustomerRequestDto(
                    name: "Test",
                    email: "test@test.com",
                    mobileNumber: "81234567"
            )

        when:
            customerService.updateCustomer(99999L, updateRequest, "TEST_USER")

        then:
            thrown(ResourceNotFoundException)
    }

    def "should get all customers"() {
        given:
            def request1 = new CustomerRequestDto(
                    name: "Customer One",
                    email: "one@test.com",
                    mobileNumber: "81234567"
            )
            def request2 = new CustomerRequestDto(
                    name: "Customer Two",
                    email: "two@test.com",
                    mobileNumber: "98765432"
            )
            customerService.createCustomer(request1, "TEST_USER")
            customerService.createCustomer(request2, "TEST_USER")

        when:
            def allCustomers = customerService.getAllCustomers()

        then:
            allCustomers.size() >= 2
            allCustomers.any { it.email == "one@test.com" }
            allCustomers.any { it.email == "two@test.com" }
    }
}

