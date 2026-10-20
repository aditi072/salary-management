package com.acme.salary.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.salary.api.dto.CountryResponse;
import com.acme.salary.api.dto.DepartmentResponse;
import com.acme.salary.repo.CountryRepository;
import com.acme.salary.repo.DepartmentRepository;

@RestController
@RequestMapping("/api")
public class ReferenceDataController {

	private final CountryRepository countries;
	private final DepartmentRepository departments;

	public ReferenceDataController(CountryRepository countries, DepartmentRepository departments) {
		this.countries = countries;
		this.departments = departments;
	}

	@GetMapping("/countries")
	public List<CountryResponse> countries() {
		return countries.findAllByOrderByNameAsc().stream()
				.map(country -> new CountryResponse(country.getCode(), country.getName(), country.getCurrencyCode()))
				.toList();
	}

	@GetMapping("/departments")
	public List<DepartmentResponse> departments() {
		return departments.findAllByOrderByNameAsc().stream()
				.map(department -> new DepartmentResponse(department.getId(), department.getName()))
				.toList();
	}
}
