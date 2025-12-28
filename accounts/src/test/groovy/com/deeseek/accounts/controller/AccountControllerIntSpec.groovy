package com.deeseek.accounts.controller

import com.deeseek.accounts.BaseIntSpec
import com.deeseek.accounts.dto.AccountDto
import com.deeseek.accounts.dto.AccountRequestDto
import com.deeseek.accounts.exception.ResourceNotFoundException
import com.deeseek.accounts.service.AccountService
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class AccountControllerIntSpec extends BaseIntSpec {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AccountService accountService = Mock()

    def setup() {
        // Verify that Spring context is loaded and MockMvc is injected
        assert mockMvc != null : "MockMvc should be injected by Spring"
        assert objectMapper != null : "ObjectMapper should be injected by Spring"
    }

    def "POST /api/accounts - should create account successfully"() {
        given:
            def requestDto = new AccountRequestDto(
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: "123 Main St"
            )
            def accountDto = AccountDto.builder()
                    .accountNumber(1234567L)
                    .customerId(1L)
                    .accountType("Savings")
                    .branchAddress("123 Main St")
                    .createdAt(LocalDate.now())
                    .createdBy("SYSTEM")
                    .build()

        when:
            def result = mockMvc.perform(
                    post("/api/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            1 * accountService.createAccount(_, "SYSTEM") >> accountDto
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath('$.statusCode').value("200"))
                    .andExpect(jsonPath('$.statusMsg').value("Account created successfully"))
                    .andExpect(jsonPath('$.data.accountNumber').value(1234567L))
                    .andExpect(jsonPath('$.data.accountType').value("Savings"))
    }

    def "POST /api/accounts - should return 400 when validation fails"() {
        given:
            def requestDto = new AccountRequestDto(
                    customerId: null,
                    accountType: "",
                    branchAddress: ""
            )

        when:
            def result = mockMvc.perform(
                    post("/api/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            0 * accountService.createAccount(_, _)
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath('$.errorCode').value("VALIDATION_FAILED"))
    }

    def "GET /api/accounts/{accountNumber} - should return account successfully"() {
        given:
            def accountDto = AccountDto.builder()
                    .accountNumber(1234567L)
                    .customerId(1L)
                    .accountType("Savings")
                    .branchAddress("123 Main St")
                    .build()

        when:
            def result = mockMvc.perform(get("/api/accounts/1234567"))

        then:
            1 * accountService.getAccount(1234567L) >> accountDto
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.statusCode').value("200"))
                    .andExpect(jsonPath('$.data.accountNumber').value(1234567L))
    }

    def "GET /api/accounts/{accountNumber} - should return 404 when account not found"() {
        when:
            def result = mockMvc.perform(get("/api/accounts/9999999"))

        then:
            1 * accountService.getAccount(9999999L) >> {
                throw new ResourceNotFoundException("Account not found with account number: 9999999")
            }
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath('$.errorCode').value("RESOURCE_NOT_FOUND"))
    }

    def "GET /api/accounts - should return all accounts"() {
        given:
            def accounts = [
                    AccountDto.builder().accountNumber(1234567L).accountType("Savings").build(),
                    AccountDto.builder().accountNumber(7654321L).accountType("Checking").build()
            ]

        when:
            def result = mockMvc.perform(get("/api/accounts"))

        then:
            1 * accountService.getAllAccounts() >> accounts
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.data').isArray())
                    .andExpect(jsonPath('$.data.length()').value(2))
    }

    def "GET /api/accounts/customer/{customerId} - should return accounts for customer"() {
        given:
            def accounts = [
                    AccountDto.builder().accountNumber(1234567L).customerId(1L).build()
            ]

        when:
            def result = mockMvc.perform(get("/api/accounts/customer/1"))

        then:
            1 * accountService.getAccountsByCustomerId(1L) >> accounts
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.data').isArray())
                    .andExpect(jsonPath('$.data.length()').value(1))
    }

    def "PUT /api/accounts/{accountNumber} - should update account successfully"() {
        given:
            def requestDto = new AccountRequestDto(
                    customerId: 1L,
                    accountType: "Checking",
                    branchAddress: "456 Oak Ave"
            )
            def accountDto = AccountDto.builder()
                    .accountNumber(1234567L)
                    .accountType("Checking")
                    .branchAddress("456 Oak Ave")
                    .build()

        when:
            def result = mockMvc.perform(
                    put("/api/accounts/1234567")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            1 * accountService.updateAccount(1234567L, _, "SYSTEM") >> accountDto
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.data.accountType').value("Checking"))
    }

    def "PATCH /api/accounts/{accountNumber} - should partially update account"() {
        given:
            def requestDto = new AccountRequestDto(
                    customerId: null,
                    accountType: "Investment",
                    branchAddress: null
            )
            def accountDto = AccountDto.builder()
                    .accountNumber(1234567L)
                    .accountType("Investment")
                    .build()

        when:
            def result = mockMvc.perform(
                    patch("/api/accounts/1234567")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
            )

        then:
            1 * accountService.partialUpdateAccount(1234567L, _, "SYSTEM") >> accountDto
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.data.accountType').value("Investment"))
    }

    def "DELETE /api/accounts/{accountNumber} - should delete account successfully"() {
        when:
            def result = mockMvc.perform(delete("/api/accounts/1234567"))

        then:
            1 * accountService.deleteAccount(1234567L)
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.statusMsg').value("Account deleted successfully"))
    }
}

