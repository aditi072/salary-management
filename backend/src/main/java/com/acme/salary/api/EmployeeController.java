package com.acme.salary.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acme.salary.api.dto.EmployeeResponse;
import com.acme.salary.api.dto.EmployeeWriteRequest;
import com.acme.salary.domain.EmploymentStatus;
import com.acme.salary.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	public Page<EmployeeResponse> list(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) String country,
			@RequestParam(required = false) Short departmentId,
			@RequestParam(required = false) EmploymentStatus status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		int pageSize = Math.min(Math.max(size, 1), 100);
		int pageNumber = Math.max(page, 0);
		var pageable = PageRequest.of(pageNumber, pageSize,
				Sort.by("lastName").ascending().and(Sort.by("firstName").ascending()));
		return employeeService.list(q, country, departmentId, status, pageable);
	}

	@GetMapping("/{id}")
	public EmployeeResponse get(@PathVariable Integer id) {
		return employeeService.get(id);
	}

	@PostMapping
	public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeWriteRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(request));
	}

	@PutMapping("/{id}")
	public EmployeeResponse update(@PathVariable Integer id, @Valid @RequestBody EmployeeWriteRequest request) {
		return employeeService.update(id, request);
	}
}
