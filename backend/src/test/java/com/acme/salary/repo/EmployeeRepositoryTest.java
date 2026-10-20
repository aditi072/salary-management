package com.acme.salary.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;
import com.acme.salary.domain.Employee;
import com.acme.salary.domain.EmploymentStatus;
import com.acme.salary.domain.Salary;

@DataJpaTest
class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository employees;

	@Autowired
	private CountryRepository countries;

	@Autowired
	private DepartmentRepository departments;

	@Test
	void filtersByCountryAndName() {
		Country india = countries.save(new Country("IN", "India", "INR"));
		Country unitedStates = countries.save(new Country("US", "United States", "USD"));
		Department engineering = departments.save(new Department("Engineering"));
		save(india, engineering, "E-1", "Asha", "Rao", EmploymentStatus.ACTIVE, "1000.00");
		save(unitedStates, engineering, "E-2", "John", "Doe", EmploymentStatus.ACTIVE, "2000.00");

		var page = employees.findAll(
				EmployeeSpecifications.filter("asha", "IN", null, null),
				PageRequest.of(0, 20));

		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getContent().get(0).getEmployeeCode()).isEqualTo("E-1");
	}

	@Test
	void payrollSummarySkipsTerminatedEmployeesAndKeepsCurrenciesApart() {
		Country india = countries.save(new Country("IN", "India", "INR"));
		Country unitedStates = countries.save(new Country("US", "United States", "USD"));
		Department engineering = departments.save(new Department("Engineering"));
		save(india, engineering, "E-1", "Asha", "Rao", EmploymentStatus.ACTIVE, "1000.00");
		save(india, engineering, "E-2", "Dev", "Shah", EmploymentStatus.ON_LEAVE, "500.00");
		save(unitedStates, engineering, "E-3", "John", "Doe", EmploymentStatus.ACTIVE, "2000.00");
		save(india, engineering, "E-4", "Old", "Timer", EmploymentStatus.TERMINATED, "9000.00");

		var byCurrency = employees.payrollByCurrency();

		assertThat(byCurrency).extracting(CurrencyTotalView::getCurrencyCode).containsExactly("INR", "USD");
		assertThat(byCurrency.get(0).getHeadcount()).isEqualTo(2);
		assertThat(byCurrency.get(0).getTotalAmount()).isEqualByComparingTo("1500.00");
		assertThat(byCurrency.get(1).getTotalAmount()).isEqualByComparingTo("2000.00");
	}

	private void save(Country country, Department department, String code, String firstName, String lastName,
			EmploymentStatus status, String amount) {
		Employee employee = new Employee();
		employee.setEmployeeCode(code);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(code.toLowerCase() + "@acme.test");
		employee.setCountry(country);
		employee.setDepartment(department);
		employee.setJobTitle("Engineer");
		employee.setEmploymentStatus(status);
		employee.setHireDate(LocalDate.of(2020, 1, 1));
		Salary salary = new Salary();
		salary.setAmount(new BigDecimal(amount));
		salary.setCurrencyCode(country.getCurrencyCode());
		salary.setEffectiveFrom(LocalDate.of(2024, 1, 1));
		employee.setSalary(salary);
		employees.save(employee);
	}
}
