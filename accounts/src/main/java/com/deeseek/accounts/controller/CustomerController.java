package com.deeseek.accounts.controller;

import com.deeseek.accounts.dto.CustomerDto;
import com.deeseek.accounts.dto.CustomerRequestDto;
import com.deeseek.accounts.dto.ResponseDto;
import com.deeseek.accounts.service.CustomerService;
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
public class CustomerController {

	private final CustomerService customerService;
	private static final String DEFAULT_USER = "SYSTEM";

	@PostMapping
	public ResponseEntity<ResponseDto<CustomerDto>> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
		CustomerDto customerDto = customerService.createCustomer(customerRequestDto, DEFAULT_USER);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseDto.success("Customer created successfully", customerDto));
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<ResponseDto<CustomerDto>> getCustomer(@PathVariable Long customerId) {
		CustomerDto customerDto = customerService.getCustomer(customerId);
		return ResponseEntity.ok(ResponseDto.success(customerDto));
	}

	@GetMapping
	public ResponseEntity<ResponseDto<List<CustomerDto>>> getAllCustomers() {
		List<CustomerDto> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(ResponseDto.success(customers));
	}

	@PutMapping("/{customerId}")
	public ResponseEntity<ResponseDto<CustomerDto>> updateCustomer(
			@PathVariable Long customerId,
			@Valid @RequestBody CustomerRequestDto customerRequestDto) {
		CustomerDto customerDto = customerService.updateCustomer(customerId, customerRequestDto, DEFAULT_USER);
		return ResponseEntity.ok(ResponseDto.success("Customer updated successfully", customerDto));
	}

	@PatchMapping("/{customerId}")
	public ResponseEntity<ResponseDto<CustomerDto>> partialUpdateCustomer(
			@PathVariable Long customerId,
			@RequestBody CustomerRequestDto customerRequestDto) {
		CustomerDto customerDto = customerService.partialUpdateCustomer(customerId, customerRequestDto, DEFAULT_USER);
		return ResponseEntity.ok(ResponseDto.success("Customer updated successfully", customerDto));
	}

	@DeleteMapping("/{customerId}")
	public ResponseEntity<ResponseDto<Void>> deleteCustomer(@PathVariable Long customerId) {
		customerService.deleteCustomer(customerId);
		return ResponseEntity.ok(ResponseDto.success("Customer deleted successfully", null));
	}

}

