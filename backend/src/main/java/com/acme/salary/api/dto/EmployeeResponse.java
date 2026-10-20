package com.acme.salary.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.acme.salary.domain.Employee;
import com.acme.salary.domain.EmploymentStatus;
import com.acme.salary.domain.Salary;

public record EmployeeResponse(
		Integer id,
		String employeeCode,
		String firstName,
		String lastName,
		String email,
		String countryCode,
		String countryName,
		Short departmentId,
		String departmentName,
		String jobTitle,
		EmploymentStatus employmentStatus,
		LocalDate hireDate,
		BigDecimal salaryAmount,
		String currencyCode,
		LocalDate effectiveFrom) {

	public static EmployeeResponse from(Employee employee) {
		Salary salary = employee.getSalary();
		if (salary == null) {
			throw new IllegalStateException("Employee " + employee.getId() + " has no current salary");
		}
		return new EmployeeResponse(
				employee.getId(),
				employee.getEmployeeCode(),
				employee.getFirstName(),
				employee.getLastName(),
				employee.getEmail(),
				employee.getCountry().getCode(),
				employee.getCountry().getName(),
				employee.getDepartment().getId(),
				employee.getDepartment().getName(),
				employee.getJobTitle(),
				employee.getEmploymentStatus(),
				employee.getHireDate(),
				salary.getAmount(),
				salary.getCurrencyCode(),
				salary.getEffectiveFrom());
	}
}
