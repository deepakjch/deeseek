package com.deebank.accountservice.controller;

import com.deebank.accountservice.dto.AccountDto;
import com.deebank.accountservice.dto.AccountRequestDto;
import com.deebank.accountservice.dto.ErrorResponseDto;
import com.deebank.accountservice.dto.ResponseDto;
import com.deebank.accountservice.service.AccountService;
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
@RequestMapping(path = "/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(name = "Account Management", description = "APIs for managing bank accounts including creation, retrieval, update, and deletion operations")
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	@Operation(
			summary = "Create a new account",
			description = "Creates a new bank account for a customer. Requires customer ID, account type, and branch address."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Account created successfully",
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
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<AccountDto>> createAccount(
			@Valid @RequestBody 
			@Parameter(description = "Account creation request", required = true)
			AccountRequestDto accountRequestDto) {
		AccountDto accountDto = accountService.createAccount(accountRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseDto.success("Account created successfully", accountDto));
	}

	@GetMapping("/{accountNumber}")
	@Operation(
			summary = "Get account by account number",
			description = "Retrieves account details for a specific account number"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Account retrieved successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Account not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<AccountDto>> getAccount(
			@PathVariable 
			@Parameter(description = "Account number", required = true, example = "1234567")
			Long accountNumber) {
		AccountDto accountDto = accountService.getAccount(accountNumber);
		return ResponseEntity.ok(ResponseDto.success(accountDto));
	}

	@GetMapping
	@Operation(
			summary = "Get all accounts",
			description = "Retrieves a list of all accounts in the system"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Accounts retrieved successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<List<AccountDto>>> getAllAccounts() {
		List<AccountDto> accounts = accountService.getAllAccounts();
		return ResponseEntity.ok(ResponseDto.success(accounts));
	}

	@GetMapping("/customer/{customerId}")
	@Operation(
			summary = "Get accounts by customer ID",
			description = "Retrieves all accounts associated with a specific customer"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Accounts retrieved successfully",
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
	public ResponseEntity<ResponseDto<List<AccountDto>>> getAccountsByCustomerId(
			@PathVariable 
			@Parameter(description = "Customer ID", required = true, example = "1")
			Long customerId) {
		List<AccountDto> accounts = accountService.getAccountsByCustomerId(customerId);
		return ResponseEntity.ok(ResponseDto.success(accounts));
	}

	@PutMapping("/{accountNumber}")
	@Operation(
			summary = "Update account",
			description = "Updates all fields of an existing account. All fields must be provided."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Account updated successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - validation errors, invalid input, or invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Account not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<AccountDto>> updateAccount(
			@PathVariable 
			@Parameter(description = "Account number", required = true, example = "1234567")
			Long accountNumber,
			@Valid @RequestBody 
			@Parameter(description = "Account update request", required = true)
			AccountRequestDto accountRequestDto) {
		AccountDto accountDto = accountService.updateAccount(accountNumber, accountRequestDto);
		return ResponseEntity.ok(ResponseDto.success("Account updated successfully", accountDto));
	}

	@PatchMapping("/{accountNumber}")
	@Operation(
			summary = "Partially update account",
			description = "Updates specific fields of an existing account. Only provided fields will be updated."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Account updated successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Account not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<AccountDto>> partialUpdateAccount(
			@PathVariable 
			@Parameter(description = "Account number", required = true, example = "1234567")
			Long accountNumber,
			@RequestBody 
			@Parameter(description = "Partial account update request", required = true)
			AccountRequestDto accountRequestDto) {
		AccountDto accountDto = accountService.partialUpdateAccount(accountNumber, accountRequestDto);
		return ResponseEntity.ok(ResponseDto.success("Account updated successfully", accountDto));
	}

	@DeleteMapping("/{accountNumber}")
	@Operation(
			summary = "Delete account",
			description = "Deletes an account from the system. This operation cannot be undone."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Account deleted successfully",
					content = @Content(schema = @Schema(implementation = ResponseDto.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Bad request - invalid parameter type",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Account not found",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error - unexpected error occurred",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
			)
	})
	public ResponseEntity<ResponseDto<Void>> deleteAccount(
			@PathVariable 
			@Parameter(description = "Account number", required = true, example = "1234567")
			Long accountNumber) {
		accountService.deleteAccount(accountNumber);
		return ResponseEntity.ok(ResponseDto.success("Account deleted successfully", null));
	}

}

