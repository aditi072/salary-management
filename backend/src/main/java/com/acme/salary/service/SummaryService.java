package com.acme.salary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acme.salary.api.dto.CountryPayrollResponse;
import com.acme.salary.api.dto.CurrencyPayrollResponse;
import com.acme.salary.api.dto.DepartmentPayrollResponse;
import com.acme.salary.api.dto.PaySummaryResponse;
import com.acme.salary.domain.PayrollMath;
import com.acme.salary.repo.EmployeeRepository;

@Service
public class SummaryService {

	private final EmployeeRepository employees;

	public SummaryService(EmployeeRepository employees) {
		this.employees = employees;
	}

	@Transactional(readOnly = true)
	public PaySummaryResponse summarize() {
		List<CurrencyPayrollResponse> byCurrency = employees.payrollByCurrency().stream()
				.map(row -> new CurrencyPayrollResponse(
						row.getCurrencyCode(),
						row.getHeadcount(),
						row.getTotalAmount(),
						PayrollMath.average(row.getTotalAmount(), row.getHeadcount())))
				.toList();
		List<CountryPayrollResponse> byCountry = employees.payrollByCountry().stream()
				.map(row -> new CountryPayrollResponse(
						row.getCountryCode(),
						row.getCountryName(),
						row.getCurrencyCode(),
						row.getHeadcount(),
						row.getTotalAmount(),
						PayrollMath.average(row.getTotalAmount(), row.getHeadcount())))
				.toList();
		List<DepartmentPayrollResponse> byDepartment = employees.payrollByDepartment().stream()
				.map(row -> new DepartmentPayrollResponse(
						row.getDepartmentId(),
						row.getDepartmentName(),
						row.getCurrencyCode(),
						row.getHeadcount(),
						row.getTotalAmount(),
						PayrollMath.average(row.getTotalAmount(), row.getHeadcount())))
				.toList();
		long paidHeadcount = byCurrency.stream().mapToLong(CurrencyPayrollResponse::headcount).sum();
		return new PaySummaryResponse(paidHeadcount, byCurrency, byCountry, byDepartment);
	}
}
