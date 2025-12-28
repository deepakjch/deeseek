package com.deeseek.accounts.dto

import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import spock.lang.Specification

class ValidationSpec extends Specification {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
    Validator validator = factory.getValidator()

    def "CustomerRequestDto - should pass validation with valid data"() {
        given:
            def dto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "john.doe@example.com",
                    mobileNumber: "81234567"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.isEmpty()
    }

    def "CustomerRequestDto - should fail validation when name is blank"() {
        given:
            def dto = new CustomerRequestDto(
                    name: "",
                    email: "john.doe@example.com",
                    mobileNumber: "81234567"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.size() == 1
            violations.any { it.propertyPath.toString() == "name" }
    }

    def "CustomerRequestDto - should fail validation when email is invalid"() {
        given:
            def dto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "invalid-email",
                    mobileNumber: "81234567"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.size() == 1
            violations.any { it.propertyPath.toString() == "email" }
    }

    def "CustomerRequestDto - should fail validation when mobile number does not start with 8 or 9"() {
        given:
            def dto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "john.doe@example.com",
                    mobileNumber: "71234567"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.size() == 1
            violations.any { it.propertyPath.toString() == "mobileNumber" }
    }

    def "CustomerRequestDto - should fail validation when mobile number is not 8 digits"() {
        given:
            def dto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "john.doe@example.com",
                    mobileNumber: "8123456"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.size() == 1
            violations.any { it.propertyPath.toString() == "mobileNumber" }
    }

    def "CustomerRequestDto - should pass validation when mobile number starts with 9"() {
        given:
            def dto = new CustomerRequestDto(
                    name: "John Doe",
                    email: "john.doe@example.com",
                    mobileNumber: "98765432"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.isEmpty()
    }

    def "AccountRequestDto - should pass validation with valid data"() {
        given:
            def dto = new AccountRequestDto(
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: "123 Main Street"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.isEmpty()
    }

    def "AccountRequestDto - should fail validation when customerId is null"() {
        given:
            def dto = new AccountRequestDto(
                    customerId: null,
                    accountType: "Savings",
                    branchAddress: "123 Main Street"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.size() == 1
            violations.any { it.propertyPath.toString() == "customerId" }
    }

    def "AccountRequestDto - should fail validation when accountType is blank"() {
        given:
            def dto = new AccountRequestDto(
                    customerId: 1L,
                    accountType: "",
                    branchAddress: "123 Main Street"
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.size() == 1
            violations.any { it.propertyPath.toString() == "accountType" }
    }

    def "AccountRequestDto - should fail validation when branchAddress is blank"() {
        given:
            def dto = new AccountRequestDto(
                    customerId: 1L,
                    accountType: "Savings",
                    branchAddress: ""
            )

        when:
            def violations = validator.validate(dto)

        then:
            violations.size() == 1
            violations.any { it.propertyPath.toString() == "branchAddress" }
    }
}

