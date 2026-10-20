package com.acme.salary.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.acme.salary.domain.Employee;
import com.acme.salary.domain.EmploymentStatus;

import jakarta.persistence.criteria.Predicate;

public final class EmployeeSpecifications {

	private EmployeeSpecifications() {
	}

	public static Specification<Employee> filter(String query, String countryCode, Short departmentId,
			EmploymentStatus status) {
		return (root, criteriaQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (query != null && !query.isBlank()) {
				String like = "%" + query.trim().toLowerCase() + "%";
				predicates.add(builder.or(
						builder.like(builder.lower(root.get("firstName")), like),
						builder.like(builder.lower(root.get("lastName")), like),
						builder.like(builder.lower(root.get("employeeCode")), like),
						builder.like(builder.lower(root.get("email")), like)));
			}
			if (countryCode != null && !countryCode.isBlank()) {
				predicates.add(builder.equal(root.get("country").get("code"), countryCode.trim().toUpperCase()));
			}
			if (departmentId != null) {
				predicates.add(builder.equal(root.get("department").get("id"), departmentId));
			}
			if (status != null) {
				predicates.add(builder.equal(root.get("employmentStatus"), status));
			}
			if (predicates.isEmpty()) {
				return builder.conjunction();
			}
			return builder.and(predicates.toArray(Predicate[]::new));
		};
	}
}
