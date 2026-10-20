package com.acme.salary.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.salary.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Short> {

	List<Department> findAllByOrderByNameAsc();
}
