package com.acme.salary.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.acme.salary.domain.EmploymentStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EmployeeWriteRequest(
		@NotBlank @Size(max = 16) String employeeCode,
		@NotBlank @Size(max = 60) String firstName,
		@NotBlank @Size(max = 60) String lastName,
		@NotBlank @Email @Size(max = 120) String email,
		@NotBlank @Size(min = 2, max = 2) String countryCode,
		@NotNull Short departmentId,
		@NotBlank @Size(max = 80) String jobTitle,
		@NotNull EmploymentStatus employmentStatus,
		@NotNull LocalDate hireDate,
		@NotNull @Positive BigDecimal salaryAmount,
		LocalDate effectiveFrom) {
}
