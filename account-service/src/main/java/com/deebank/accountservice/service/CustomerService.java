package com.deebank.accountservice.service;

import com.deebank.accountservice.dto.CustomerDto;
import com.deebank.accountservice.dto.CustomerRequestDto;
import com.deebank.accountservice.entity.Customer;
import com.deebank.accountservice.exception.OperationNotAllowedException;
import com.deebank.accountservice.exception.ResourceAlreadyExistsException;
import com.deebank.accountservice.exception.ResourceNotFoundException;
import com.deebank.accountservice.repository.AccountRepository;
import com.deebank.accountservice.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final AccountRepository accountRepository;

	public CustomerDto createCustomer(CustomerRequestDto requestDto) {
		// Check if email already exists
		if (customerRepository.findByEmail(requestDto.getEmail()).isPresent()) {
			throw new ResourceAlreadyExistsException("Customer with email " + requestDto.getEmail() + " already exists");
		}

		// Check if mobile number already exists
		if (customerRepository.findByMobileNumber(requestDto.getMobileNumber()).isPresent()) {
			throw new ResourceAlreadyExistsException("Customer with mobile number " + requestDto.getMobileNumber() + " already exists");
		}

		Customer customer = new Customer();
		customer.setName(requestDto.getName());
		customer.setEmail(requestDto.getEmail());
		customer.setMobileNumber(requestDto.getMobileNumber());
		// Audit fields (createdAt, createdBy, updatedAt, updatedBy) are automatically set by JPA auditing

		Customer savedCustomer = customerRepository.save(customer);
		return mapToDto(savedCustomer);
	}

	public CustomerDto getCustomer(Long customerId) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null");
		}
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
		return mapToDto(customer);
	}

	public List<CustomerDto> getAllCustomers() {
		return customerRepository.findAll().stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public CustomerDto updateCustomer(Long customerId, CustomerRequestDto requestDto) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null");
		}
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

		// Check if email is being changed and if it already exists
		if (!customer.getEmail().equals(requestDto.getEmail())) {
			if (customerRepository.findByEmail(requestDto.getEmail()).isPresent()) {
				throw new ResourceAlreadyExistsException("Customer with email " + requestDto.getEmail() + " already exists");
			}
		}

		// Check if mobile number is being changed and if it already exists
		if (!customer.getMobileNumber().equals(requestDto.getMobileNumber())) {
			if (customerRepository.findByMobileNumber(requestDto.getMobileNumber()).isPresent()) {
				throw new ResourceAlreadyExistsException("Customer with mobile number " + requestDto.getMobileNumber() + " already exists");
			}
		}

		customer.setName(requestDto.getName());
		customer.setEmail(requestDto.getEmail());
		customer.setMobileNumber(requestDto.getMobileNumber());
		// Audit fields (updatedAt, updatedBy) are automatically set by JPA auditing

		Customer updatedCustomer = customerRepository.save(customer);
		return mapToDto(updatedCustomer);
	}

	public CustomerDto partialUpdateCustomer(Long customerId, CustomerRequestDto requestDto) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null");
		}
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

		if (requestDto.getName() != null) {
			customer.setName(requestDto.getName());
		}
		if (requestDto.getEmail() != null && !customer.getEmail().equals(requestDto.getEmail())) {
			if (customerRepository.findByEmail(requestDto.getEmail()).isPresent()) {
				throw new ResourceAlreadyExistsException("Customer with email " + requestDto.getEmail() + " already exists");
			}
			customer.setEmail(requestDto.getEmail());
		}
		if (requestDto.getMobileNumber() != null && !customer.getMobileNumber().equals(requestDto.getMobileNumber())) {
			if (customerRepository.findByMobileNumber(requestDto.getMobileNumber()).isPresent()) {
				throw new ResourceAlreadyExistsException("Customer with mobile number " + requestDto.getMobileNumber() + " already exists");
			}
			customer.setMobileNumber(requestDto.getMobileNumber());
		}
		// Audit fields (updatedAt, updatedBy) are automatically set by JPA auditing

		Customer updatedCustomer = customerRepository.save(customer);
		return mapToDto(updatedCustomer);
	}

	public void deleteCustomer(Long customerId) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null");
		}
		Customer customer = Objects.requireNonNull(
				customerRepository.findById(customerId)
						.orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId)),
				"Customer cannot be null"
		);

		// Check if customer has accounts
		if (!accountRepository.findByCustomerId(customerId).isEmpty()) {
			throw new OperationNotAllowedException("Cannot delete customer with id " + customerId + " because they have associated accounts");
		}

		customerRepository.delete(customer);
	}

	private CustomerDto mapToDto(Customer customer) {
		Customer nonNullCustomer = Objects.requireNonNull(customer, "Customer cannot be null");
		Long customerId = Objects.requireNonNull(nonNullCustomer.getCustomerId(), "Customer ID cannot be null");
		// Get account numbers for this customer
		List<Long> accountNumbers = accountRepository.findByCustomerId(customerId).stream()
				.map(account -> account.getAccountNumber())
				.collect(Collectors.toList());

		return CustomerDto.builder()
				.customerId(customerId)
				.name(nonNullCustomer.getName())
				.email(nonNullCustomer.getEmail())
				.mobileNumber(nonNullCustomer.getMobileNumber())
				.accountNumbers(accountNumbers)
				.createdAt(nonNullCustomer.getCreatedAt())
				.createdBy(nonNullCustomer.getCreatedBy())
				.updatedAt(nonNullCustomer.getUpdatedAt())
				.updatedBy(nonNullCustomer.getUpdatedBy())
				.build();
	}

}

