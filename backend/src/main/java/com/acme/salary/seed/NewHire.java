package com.acme.salary.seed;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;
import com.acme.salary.domain.EmploymentStatus;

public record NewHire(
		String employeeCode,
		String firstName,
		String lastName,
		String email,
		Country country,
		Department department,
		String jobTitle,
		EmploymentStatus employmentStatus,
		LocalDate hireDate,
		BigDecimal salaryAmount,
		String currencyCode,
		LocalDate effectiveFrom) {
}
