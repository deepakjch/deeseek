package com.deebank.accountservice.controller;

import com.deebank.accountservice.dto.CustomerDto;
import com.deebank.accountservice.dto.CustomerRequestDto;
import com.deebank.accountservice.dto.ErrorResponseDto;
import com.deebank.accountservice.dto.ResponseDto;
import com.deebank.accountservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/customers", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(name = "Customer Management", description = "APIs for managing customers including creation, retrieval, update, and deletion operations")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping
	@Operation(
			summary = "Create a new customer",
			description = "Creates a new customer in the system. Requires name, email, and mobile number. Email and mobile number must be unique."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Customer created successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - validation errors or invalid input",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "409",
					description = "Conflict - customer with email or mobile number already exists",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<CustomerDto>> createCustomer(
			@Valid @RequestBody 
			@Parameter(description = "Customer creation request", required = true)
			CustomerRequestDto customerRequestDto) {
		CustomerDto customerDto = customerService.createCustomer(customerRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseDto.success("Customer created successfully", customerDto));
	}

	@GetMapping("/{customerId}")
	@Operation(
			summary = "Get customer by ID",
			description = "Retrieves customer details including associated account numbers for a specific customer ID"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Customer retrieved successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Customer not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<CustomerDto>> getCustomer(
			@PathVariable 
			@Parameter(description = "Customer ID", required = true, example = "1")
			Long customerId) {
		CustomerDto customerDto = customerService.getCustomer(customerId);
		return ResponseEntity.ok(ResponseDto.success(customerDto));
	}

	@GetMapping
	@Operation(
			summary = "Get all customers",
			description = "Retrieves a list of all customers in the system"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Customers retrieved successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<List<CustomerDto>>> getAllCustomers() {
		List<CustomerDto> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(ResponseDto.success(customers));
	}

	@PutMapping("/{customerId}")
	@Operation(
			summary = "Update customer",
			description = "Updates all fields of an existing customer. All fields must be provided."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Customer updated successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - validation errors, invalid input, or invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Customer not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "409",
					description = "Conflict - customer with email or mobile number already exists",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<CustomerDto>> updateCustomer(
			@PathVariable 
			@Parameter(description = "Customer ID", required = true, example = "1")
			Long customerId,
			@Valid @RequestBody 
			@Parameter(description = "Customer update request", required = true)
			CustomerRequestDto customerRequestDto) {
		CustomerDto customerDto = customerService.updateCustomer(customerId, customerRequestDto);
		return ResponseEntity.ok(ResponseDto.success("Customer updated successfully", customerDto));
	}

	@PatchMapping("/{customerId}")
	@Operation(
			summary = "Partially update customer",
			description = "Updates specific fields of an existing customer. Only provided fields will be updated."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Customer updated successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Customer not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "409",
					description = "Conflict - customer with email or mobile number already exists",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<CustomerDto>> partialUpdateCustomer(
			@PathVariable 
			@Parameter(description = "Customer ID", required = true, example = "1")
			Long customerId,
			@RequestBody 
			@Parameter(description = "Partial customer update request", required = true)
			CustomerRequestDto customerRequestDto) {
		CustomerDto customerDto = customerService.partialUpdateCustomer(customerId, customerRequestDto);
		return ResponseEntity.ok(ResponseDto.success("Customer updated successfully", customerDto));
	}

	@DeleteMapping("/{customerId}")
	@Operation(
			summary = "Delete customer",
			description = "Deletes a customer from the system. Customer must not have any associated accounts. This operation cannot be undone."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Customer deleted successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - customer has associated accounts or invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Customer not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<Void>> deleteCustomer(
			@PathVariable 
			@Parameter(description = "Customer ID", required = true, example = "1")
			Long customerId) {
		customerService.deleteCustomer(customerId);
		return ResponseEntity.ok(ResponseDto.success("Customer deleted successfully", null));
	}

}

