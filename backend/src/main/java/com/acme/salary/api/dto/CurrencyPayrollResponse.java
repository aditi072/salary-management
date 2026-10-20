package com.acme.salary.api.dto;

import java.math.BigDecimal;

public record CurrencyPayrollResponse(
		String currencyCode,
		long headcount,
		BigDecimal totalAmount,
		BigDecimal averageAmount) {
}
