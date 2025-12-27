package com.deeseek.accounts.repository;

import com.deeseek.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountNumber(Long accountNumber);

	List<Account> findByCustomerId(Long customerId);

	boolean existsByAccountNumber(Long accountNumber);

}

