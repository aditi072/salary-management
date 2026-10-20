package com.acme.salary.seed;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;
import com.acme.salary.domain.Employee;
import com.acme.salary.domain.EmploymentStatus;
import com.acme.salary.domain.Salary;
import com.acme.salary.repo.CountryRepository;
import com.acme.salary.repo.DepartmentRepository;
import com.acme.salary.repo.EmployeeRepository;

@DataJpaTest
@Import(SeedService.class)
class SeedServiceTest {

	@Autowired
	private SeedService seedService;

	@Autowired
	private EmployeeRepository employees;

	@Autowired
	private CountryRepository countries;

	@Autowired
	private DepartmentRepository departments;

	@Test
	void insertsUntilTheTargetThenLeavesExistingRowsAlone() {
		countries.save(new Country("IN", "India", "INR"));
		countries.save(new Country("US", "United States", "USD"));
		departments.save(new Department("Engineering"));
		departments.save(new Department("Sales"));

		assertThat(seedService.seedUpTo(30)).isEqualTo(30);
		assertThat(employees.count()).isEqualTo(30);
		assertThat(seedService.seedUpTo(30)).isZero();
		assertThat(employees.findAll()).allSatisfy(employee -> {
			assertThat(employee.getSalary().getAmount()).isGreaterThan(BigDecimal.ZERO);
			assertThat(employee.getSalary().getCurrencyCode()).isEqualTo(employee.getCountry().getCurrencyCode());
		});
	}

	@Test
	void fillsUpToTheTargetWithoutReplacingAnExistingPerson() {
		Country india = countries.save(new Country("IN", "India", "INR"));
		Department engineering = departments.save(new Department("Engineering"));
		Employee existing = new Employee();
		existing.setEmployeeCode("API-CHECK-01");
		existing.setFirstName("Ada");
		existing.setLastName("Lovelace");
		existing.setEmail("ada@acme.test");
		existing.setCountry(india);
		existing.setDepartment(engineering);
		existing.setJobTitle("Engineer");
		existing.setEmploymentStatus(EmploymentStatus.ACTIVE);
		existing.setHireDate(LocalDate.of(2020, 1, 1));
		Salary salary = new Salary();
		salary.setAmount(new BigDecimal("1000.00"));
		salary.setCurrencyCode("INR");
		salary.setEffectiveFrom(LocalDate.of(2020, 1, 1));
		existing.setSalary(salary);
		employees.save(existing);

		assertThat(seedService.seedUpTo(5)).isEqualTo(4);
		assertThat(employees.count()).isEqualTo(5);
		assertThat(employees.findAll()).extracting(Employee::getEmployeeCode).contains("API-CHECK-01", "EMP00001");
	}
}
