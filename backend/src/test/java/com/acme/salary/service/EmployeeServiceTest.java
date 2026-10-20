package com.acme.salary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.salary.api.dto.EmployeeWriteRequest;
import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;
import com.acme.salary.domain.Employee;
import com.acme.salary.domain.EmploymentStatus;
import com.acme.salary.repo.CountryRepository;
import com.acme.salary.repo.DepartmentRepository;
import com.acme.salary.repo.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employees;

	@Mock
	private CountryRepository countries;

	@Mock
	private DepartmentRepository departments;

	private EmployeeService service;

	@BeforeEach
	void setUp() {
		service = new EmployeeService(employees, countries, departments);
	}

	@Test
	void createStoresSalaryInTheCountryCurrency() {
		when(employees.existsByEmployeeCodeIgnoreCase("E-1")).thenReturn(false);
		when(employees.existsByEmailIgnoreCase("asha@acme.test")).thenReturn(false);
		when(countries.findById("IN")).thenReturn(Optional.of(new Country("IN", "India", "INR")));
		when(departments.findById((short) 1)).thenReturn(Optional.of(new Department("Engineering")));
		when(employees.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

		var response = service.create(request(new BigDecimal("1800000.00"), "IN"));

		assertThat(response.currencyCode()).isEqualTo("INR");
		assertThat(response.salaryAmount()).isEqualByComparingTo("1800000.00");
		ArgumentCaptor<Employee> saved = ArgumentCaptor.forClass(Employee.class);
		verify(employees).save(saved.capture());
		assertThat(saved.getValue().getSalary().getCurrencyCode()).isEqualTo("INR");
	}

	@Test
	void createRejectsASalaryThatIsNotPositive() {
		assertThatThrownBy(() -> service.create(request(BigDecimal.ZERO, "IN")))
				.isInstanceOf(BadRequestException.class);
		verify(employees, never()).save(any());
	}

	@Test
	void createRejectsAnUnknownCountry() {
		when(employees.existsByEmployeeCodeIgnoreCase("E-1")).thenReturn(false);
		when(employees.existsByEmailIgnoreCase("asha@acme.test")).thenReturn(false);
		when(countries.findById("IN")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.create(request(new BigDecimal("10.00"), "IN")))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void updateSwitchesCurrencyWhenTheCountryChanges() {
		Employee existing = new Employee();
		existing.setEmployeeCode("E-1");
		existing.setEmail("asha@acme.test");
		existing.setCountry(new Country("IN", "India", "INR"));
		var salary = new com.acme.salary.domain.Salary();
		salary.setAmount(new BigDecimal("100.00"));
		salary.setCurrencyCode("INR");
		salary.setEffectiveFrom(LocalDate.of(2024, 1, 1));
		existing.setSalary(salary);
		when(employees.findById(7)).thenReturn(Optional.of(existing));
		when(employees.existsByEmployeeCodeIgnoreCaseAndIdNot("E-1", 7)).thenReturn(false);
		when(employees.existsByEmailIgnoreCaseAndIdNot("asha@acme.test", 7)).thenReturn(false);
		when(countries.findById("US")).thenReturn(Optional.of(new Country("US", "United States", "USD")));
		when(departments.findById((short) 1)).thenReturn(Optional.of(new Department("Engineering")));

		var response = service.update(7, request(new BigDecimal("90000.00"), "US"));

		assertThat(response.currencyCode()).isEqualTo("USD");
		assertThat(existing.getSalary().getAmount()).isEqualByComparingTo("90000.00");
	}

	private EmployeeWriteRequest request(BigDecimal amount, String countryCode) {
		return new EmployeeWriteRequest(
				"E-1",
				"Asha",
				"Rao",
				"asha@acme.test",
				countryCode,
				(short) 1,
				"Engineer",
				EmploymentStatus.ACTIVE,
				LocalDate.of(2022, 4, 1),
				amount,
				LocalDate.of(2024, 1, 1));
	}
}
