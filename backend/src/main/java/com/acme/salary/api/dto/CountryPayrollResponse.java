package com.acme.salary.api.dto;

import java.math.BigDecimal;

public record CountryPayrollResponse(
		String countryCode,
		String countryName,
		String currencyCode,
		long headcount,
		BigDecimal totalAmount,
		BigDecimal averageAmount) {
}
