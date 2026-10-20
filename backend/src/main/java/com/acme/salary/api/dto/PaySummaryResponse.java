package com.acme.salary.api.dto;

import java.util.List;

public record PaySummaryResponse(
		long paidHeadcount,
		List<CurrencyPayrollResponse> byCurrency,
		List<CountryPayrollResponse> byCountry,
		List<DepartmentPayrollResponse> byDepartment) {
}
