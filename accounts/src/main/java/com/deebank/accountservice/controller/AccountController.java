package com.deebank.accountservice.controller;

import com.deebank.accountservice.dto.AccountDto;
import com.deebank.accountservice.dto.AccountRequestDto;
import com.deebank.accountservice.dto.ResponseDto;
import com.deebank.accountservice.service.AccountService;
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
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	public ResponseEntity<ResponseDto<AccountDto>> createAccount(@Valid @RequestBody AccountRequestDto accountRequestDto) {
		AccountDto accountDto = accountService.createAccount(accountRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseDto.success("Account created successfully", accountDto));
	}

	@GetMapping("/{accountNumber}")
	public ResponseEntity<ResponseDto<AccountDto>> getAccount(@PathVariable Long accountNumber) {
		AccountDto accountDto = accountService.getAccount(accountNumber);
		return ResponseEntity.ok(ResponseDto.success(accountDto));
	}

	@GetMapping
	public ResponseEntity<ResponseDto<List<AccountDto>>> getAllAccounts() {
		List<AccountDto> accounts = accountService.getAllAccounts();
		return ResponseEntity.ok(ResponseDto.success(accounts));
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<ResponseDto<List<AccountDto>>> getAccountsByCustomerId(@PathVariable Long customerId) {
		List<AccountDto> accounts = accountService.getAccountsByCustomerId(customerId);
		return ResponseEntity.ok(ResponseDto.success(accounts));
	}

	@PutMapping("/{accountNumber}")
	public ResponseEntity<ResponseDto<AccountDto>> updateAccount(
			@PathVariable Long accountNumber,
			@Valid @RequestBody AccountRequestDto accountRequestDto) {
		AccountDto accountDto = accountService.updateAccount(accountNumber, accountRequestDto);
		return ResponseEntity.ok(ResponseDto.success("Account updated successfully", accountDto));
	}

	@PatchMapping("/{accountNumber}")
	public ResponseEntity<ResponseDto<AccountDto>> partialUpdateAccount(
			@PathVariable Long accountNumber,
			@RequestBody AccountRequestDto accountRequestDto) {
		AccountDto accountDto = accountService.partialUpdateAccount(accountNumber, accountRequestDto);
		return ResponseEntity.ok(ResponseDto.success("Account updated successfully", accountDto));
	}

	@DeleteMapping("/{accountNumber}")
	public ResponseEntity<ResponseDto<Void>> deleteAccount(@PathVariable Long accountNumber) {
		accountService.deleteAccount(accountNumber);
		return ResponseEntity.ok(ResponseDto.success("Account deleted successfully", null));
	}

}

