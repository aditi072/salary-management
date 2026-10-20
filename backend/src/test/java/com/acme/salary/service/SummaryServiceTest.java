package com.acme.salary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.salary.repo.CurrencyTotalView;
import com.acme.salary.repo.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

	@Mock
	private EmployeeRepository employees;

	@Test
	void keepsEachCurrencySeparateAndAveragesWithinIt() {
		when(employees.payrollByCurrency()).thenReturn(List.of(
				currency("INR", 2, "3000.00"),
				currency("USD", 1, "1000.00")));
		when(employees.payrollByCountry()).thenReturn(List.of());
		when(employees.payrollByDepartment()).thenReturn(List.of());

		var summary = new SummaryService(employees).summarize();

		assertThat(summary.paidHeadcount()).isEqualTo(3);
		assertThat(summary.byCurrency()).hasSize(2);
		assertThat(summary.byCurrency().get(0).averageAmount()).isEqualByComparingTo("1500.00");
		assertThat(summary.byCurrency().get(1).averageAmount()).isEqualByComparingTo("1000.00");
		assertThat(summary.byCurrency()).extracting(row -> row.currencyCode()).containsExactly("INR", "USD");
	}

	private static CurrencyTotalView currency(String code, long headcount, String total) {
		return new CurrencyTotalView() {
			@Override
			public String getCurrencyCode() {
				return code;
			}

			@Override
			public long getHeadcount() {
				return headcount;
			}

			@Override
			public BigDecimal getTotalAmount() {
				return new BigDecimal(total);
			}
		};
	}
}
