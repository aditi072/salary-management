package com.acme.salary.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acme.salary.api.dto.EmployeeResponse;
import com.acme.salary.api.dto.EmployeeWriteRequest;
import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;
import com.acme.salary.domain.Employee;
import com.acme.salary.domain.EmploymentStatus;
import com.acme.salary.domain.Salary;
import com.acme.salary.repo.CountryRepository;
import com.acme.salary.repo.DepartmentRepository;
import com.acme.salary.repo.EmployeeRepository;
import com.acme.salary.repo.EmployeeSpecifications;

@Service
public class EmployeeService {

	private final EmployeeRepository employees;
	private final CountryRepository countries;
	private final DepartmentRepository departments;

	public EmployeeService(EmployeeRepository employees, CountryRepository countries,
			DepartmentRepository departments) {
		this.employees = employees;
		this.countries = countries;
		this.departments = departments;
	}

	@Transactional(readOnly = true)
	public Page<EmployeeResponse> list(String query, String countryCode, Short departmentId,
			EmploymentStatus status, Pageable pageable) {
		return employees.findAll(EmployeeSpecifications.filter(query, countryCode, departmentId, status), pageable)
				.map(EmployeeResponse::from);
	}

	@Transactional(readOnly = true)
	public EmployeeResponse get(Integer id) {
		return EmployeeResponse.from(findEmployee(id));
	}

	@Transactional
	public EmployeeResponse create(EmployeeWriteRequest request) {
		if (employees.existsByEmployeeCodeIgnoreCase(request.employeeCode().trim())) {
			throw new ConflictException("An employee with this code already exists");
		}
		if (employees.existsByEmailIgnoreCase(request.email().trim())) {
			throw new ConflictException("An employee with this email already exists");
		}
		Employee employee = new Employee();
		apply(employee, request, true);
		return EmployeeResponse.from(employees.save(employee));
	}

	@Transactional
	public EmployeeResponse update(Integer id, EmployeeWriteRequest request) {
		Employee employee = findEmployee(id);
		if (employees.existsByEmployeeCodeIgnoreCaseAndIdNot(request.employeeCode().trim(), id)) {
			throw new ConflictException("An employee with this code already exists");
		}
		if (employees.existsByEmailIgnoreCaseAndIdNot(request.email().trim(), id)) {
			throw new ConflictException("An employee with this email already exists");
		}
		apply(employee, request, false);
		return EmployeeResponse.from(employee);
	}

	private Employee findEmployee(Integer id) {
		return employees.findById(id).orElseThrow(() -> new NotFoundException("Employee not found"));
	}

	private void apply(Employee employee, EmployeeWriteRequest request, boolean creating) {
		if (request.salaryAmount().signum() <= 0) {
			throw new BadRequestException("Salary amount must be greater than zero");
		}
		Country country = countries.findById(request.countryCode().trim().toUpperCase())
				.orElseThrow(() -> new NotFoundException("Country not found"));
		Department department = departments.findById(request.departmentId())
				.orElseThrow(() -> new NotFoundException("Department not found"));

		employee.setEmployeeCode(request.employeeCode().trim());
		employee.setFirstName(request.firstName().trim());
		employee.setLastName(request.lastName().trim());
		employee.setEmail(request.email().trim().toLowerCase());
		employee.setCountry(country);
		employee.setDepartment(department);
		employee.setJobTitle(request.jobTitle().trim());
		employee.setEmploymentStatus(request.employmentStatus());
		employee.setHireDate(request.hireDate());

		Salary salary = employee.getSalary();
		if (salary == null) {
			salary = new Salary();
			employee.setSalary(salary);
		}
		salary.setAmount(request.salaryAmount());
		salary.setCurrencyCode(country.getCurrencyCode());
		LocalDate effectiveFrom = request.effectiveFrom();
		if (effectiveFrom == null && (creating || salary.getEffectiveFrom() == null)) {
			effectiveFrom = LocalDate.now();
		}
		if (effectiveFrom != null) {
			salary.setEffectiveFrom(effectiveFrom);
		}
	}
}
