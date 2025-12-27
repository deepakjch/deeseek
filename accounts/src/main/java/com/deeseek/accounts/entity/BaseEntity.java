package com.deeseek.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDate createdAt;

	@Column(name = "created_by", nullable = false, updatable = false)
	private String createdBy;

	@Column(name = "updated_at")
	private LocalDate updatedAt;

	@Column(name = "updated_by")
	private String updatedBy;

}

