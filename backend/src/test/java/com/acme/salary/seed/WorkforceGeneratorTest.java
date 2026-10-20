package com.acme.salary.seed;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;

class WorkforceGeneratorTest {

	private final List<Country> countries = List.of(
			new Country("IN", "India", "INR"),
			new Country("US", "United States", "USD"),
			new Country("GB", "United Kingdom", "GBP"),
			new Country("DE", "Germany", "EUR"),
			new Country("SG", "Singapore", "SGD"),
			new Country("AE", "United Arab Emirates", "AED"));

	private final List<Department> departments = List.of(
			new Department("Engineering"),
			new Department("Product"),
			new Department("Design"),
			new Department("People"),
			new Department("Finance"),
			new Department("Sales"),
			new Department("Marketing"),
			new Department("Operations"),
			new Department("Customer Support"),
			new Department("Legal"));

	@Test
	void buildsTenThousandDistinctEmployees() {
		List<NewHire> hires = WorkforceGenerator.generate(10_000, countries, departments);

		assertThat(hires).hasSize(10_000);
		assertThat(hires).extracting(NewHire::employeeCode).doesNotHaveDuplicates();
		assertThat(hires).extracting(NewHire::email).doesNotHaveDuplicates();
		assertThat(hires).allSatisfy(hire -> {
			assertThat(hire.salaryAmount()).isGreaterThan(BigDecimal.ZERO);
			assertThat(hire.currencyCode()).isEqualTo(hire.country().getCurrencyCode());
			assertThat(hire.effectiveFrom()).isAfterOrEqualTo(hire.hireDate());
			assertThat(hire.effectiveFrom()).isBeforeOrEqualTo(WorkforceGenerator.TODAY);
		});
		assertThat(hires).extracting(hire -> hire.country().getCode()).contains("IN", "US", "DE", "AE");
		assertThat(hires).extracting(hire -> hire.department().getName()).contains("Engineering", "Legal");
	}
}
