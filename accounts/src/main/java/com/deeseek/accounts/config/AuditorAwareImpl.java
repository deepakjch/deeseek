package com.deeseek.accounts.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	private static final String AUDITOR_NAME = "Account Service";

	@Override
	@NonNull
	@SuppressWarnings("null")
	public Optional<String> getCurrentAuditor() {
		return Optional.of(AUDITOR_NAME);
	}
}

