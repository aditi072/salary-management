package com.acme.salary.api.dto;

import java.math.BigDecimal;

public record DepartmentPayrollResponse(
		Short departmentId,
		String departmentName,
		String currencyCode,
		long headcount,
		BigDecimal totalAmount,
		BigDecimal averageAmount) {
}
