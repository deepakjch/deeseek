package com.deeseek.accounts.controller

import com.deeseek.accounts.dto.CustomerDto
import com.deeseek.accounts.dto.CustomerRequestDto
import com.deeseek.accounts.exception.ResourceAlreadyExistsException
import com.deeseek.accounts.exception.ResourceNotFoundException
import com.deeseek.accounts.exception.OperationNotAllowedException
import com.deeseek.accounts.service.CustomerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(CustomerController)
class CustomerControllerIntegrationTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    CustomerService customerService = Mock()

    def "POST /api/customers - should create customer successfully"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "john.doe@example.com",
                    mobileNumber: "81234567"
            )
            def customerDto = CustomerDto.builder()
                    .customerId(1L)
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .mobileNumber("81234567")
                    .createdAt(LocalDate.now())
                    .createdBy("SYSTEM")
                    .build()

        when:
            def result = mockMvc.perform(
                    post("/api/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            1 * customerService.createCustomer(_, "SYSTEM") >> customerDto
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath('$.statusCode').value("200"))
                    .andExpect(jsonPath('$.statusMsg').value("Customer created successfully"))
                    .andExpect(jsonPath('$.data.customerId').value(1L))
                    .andExpect(jsonPath('$.data.name').value("John Doe"))
                    .andExpect(jsonPath('$.data.email').value("john.doe@example.com"))
    }

    def "POST /api/customers - should return 409 when customer email already exists"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "existing@example.com",
                    mobileNumber: "81234567"
            )

        when:
            def result = mockMvc.perform(
                    post("/api/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            1 * customerService.createCustomer(_, "SYSTEM") >> {
                throw new ResourceAlreadyExistsException("Customer with email existing@example.com already exists")
            }
            result.andExpect(status().isConflict())
                    .andExpect(jsonPath('$.errorCode').value("RESOURCE_ALREADY_EXISTS"))
    }

    def "POST /api/customers - should return 400 when validation fails"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "",
                    email: "invalid-email",
                    mobileNumber: "123"
            )

        when:
            def result = mockMvc.perform(
                    post("/api/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            0 * customerService.createCustomer(_, _)
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath('$.errorCode').value("VALIDATION_FAILED"))
                    .andExpect(jsonPath('$.validationErrors').isArray())
    }

    def "GET /api/customers/{customerId} - should return customer successfully"() {
        given:
            def customerDto = CustomerDto.builder()
                    .customerId(1L)
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .mobileNumber("81234567")
                    .build()

        when:
            def result = mockMvc.perform(get("/api/customers/1"))

        then:
            1 * customerService.getCustomer(1L) >> customerDto
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.statusCode').value("200"))
                    .andExpect(jsonPath('$.data.customerId').value(1L))
                    .andExpect(jsonPath('$.data.name').value("John Doe"))
    }

    def "GET /api/customers/{customerId} - should return 404 when customer not found"() {
        when:
            def result = mockMvc.perform(get("/api/customers/999"))

        then:
            1 * customerService.getCustomer(999L) >> {
                throw new ResourceNotFoundException("Customer not found with id: 999")
            }
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath('$.errorCode').value("RESOURCE_NOT_FOUND"))
    }

    def "GET /api/customers - should return all customers"() {
        given:
            def customers = [
                    CustomerDto.builder().customerId(1L).name("John Doe").build(),
                    CustomerDto.builder().customerId(2L).name("Jane Doe").build()
            ]

        when:
            def result = mockMvc.perform(get("/api/customers"))

        then:
            1 * customerService.getAllCustomers() >> customers
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.statusCode').value("200"))
                    .andExpect(jsonPath('$.data').isArray())
                    .andExpect(jsonPath('$.data.length()').value(2))
    }

    def "PUT /api/customers/{customerId} - should update customer successfully"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "John Updated",
                    email: "john.updated@example.com",
                    mobileNumber: "98765432"
            )
            def customerDto = CustomerDto.builder()
                    .customerId(1L)
                    .name("John Updated")
                    .email("john.updated@example.com")
                    .mobileNumber("98765432")
                    .build()

        when:
            def result = mockMvc.perform(
                    put("/api/customers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            1 * customerService.updateCustomer(1L, _, "SYSTEM") >> customerDto
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.statusCode').value("200"))
                    .andExpect(jsonPath('$.data.name').value("John Updated"))
    }

    def "PATCH /api/customers/{customerId} - should partially update customer"() {
        given:
            def requestDto = new CustomerRequestDto(
                    name: "John Patched",
                    email: null,
                    mobileNumber: null
            )
            def customerDto = CustomerDto.builder()
                    .customerId(1L)
                    .name("John Patched")
                    .email("john.doe@example.com")
                    .mobileNumber("81234567")
                    .build()

        when:
            def result = mockMvc.perform(
                    patch("/api/customers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            1 * customerService.partialUpdateCustomer(1L, _, "SYSTEM") >> customerDto
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.data.name').value("John Patched"))
    }

    def "DELETE /api/customers/{customerId} - should delete customer successfully"() {
        when:
            def result = mockMvc.perform(delete("/api/customers/1"))

        then:
            1 * customerService.deleteCustomer(1L)
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.statusCode').value("200"))
                    .andExpect(jsonPath('$.statusMsg').value("Customer deleted successfully"))
    }

    def "DELETE /api/customers/{customerId} - should return 400 when customer has associated accounts"() {
        when:
            def result = mockMvc.perform(delete("/api/customers/1"))

        then:
            1 * customerService.deleteCustomer(1L) >> {
                throw new OperationNotAllowedException("Cannot delete customer with id 1 because they have associated accounts")
            }
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath('$.errorCode').value("OPERATION_NOT_ALLOWED"))
    }
}

