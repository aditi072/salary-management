package com.acme.salary.seed;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;
import com.acme.salary.domain.Employee;
import com.acme.salary.domain.Salary;
import com.acme.salary.repo.CountryRepository;
import com.acme.salary.repo.DepartmentRepository;
import com.acme.salary.repo.EmployeeRepository;

import jakarta.persistence.EntityManager;

@Service
public class SeedService {

	static final int BATCH_SIZE = 200;

	private final EmployeeRepository employees;
	private final CountryRepository countries;
	private final DepartmentRepository departments;
	private final EntityManager entityManager;

	public SeedService(EmployeeRepository employees, CountryRepository countries, DepartmentRepository departments,
			EntityManager entityManager) {
		this.employees = employees;
		this.countries = countries;
		this.departments = departments;
		this.entityManager = entityManager;
	}

	@Transactional
	public int seedUpTo(int target) {
		long existing = employees.count();
		if (existing >= target) {
			return 0;
		}
		int missing = (int) (target - existing);
		List<Country> countryRows = countries.findAll();
		List<Department> departmentRows = departments.findAll();
		List<NewHire> hires = WorkforceGenerator.generate(missing, nextEmployeeNumber(), countryRows, departmentRows);
		for (int start = 0; start < hires.size(); start += BATCH_SIZE) {
			int end = Math.min(start + BATCH_SIZE, hires.size());
			for (NewHire hire : hires.subList(start, end)) {
				entityManager.persist(toEmployee(hire));
			}
			entityManager.flush();
			entityManager.clear();
		}
		return hires.size();
	}

	private int nextEmployeeNumber() {
		String maxCode = employees.maxSeedCode();
		if (maxCode == null || !maxCode.startsWith("EMP")) {
			return 1;
		}
		return Integer.parseInt(maxCode.substring(3)) + 1;
	}

	private Employee toEmployee(NewHire hire) {
		Employee employee = new Employee();
		employee.setEmployeeCode(hire.employeeCode());
		employee.setFirstName(hire.firstName());
		employee.setLastName(hire.lastName());
		employee.setEmail(hire.email());
		employee.setCountry(entityManager.getReference(Country.class, hire.country().getCode()));
		employee.setDepartment(entityManager.getReference(Department.class, hire.department().getId()));
		employee.setJobTitle(hire.jobTitle());
		employee.setEmploymentStatus(hire.employmentStatus());
		employee.setHireDate(hire.hireDate());
		Salary salary = new Salary();
		salary.setAmount(hire.salaryAmount());
		salary.setCurrencyCode(hire.currencyCode());
		salary.setEffectiveFrom(hire.effectiveFrom());
		employee.setSalary(salary);
		return employee;
	}
}
