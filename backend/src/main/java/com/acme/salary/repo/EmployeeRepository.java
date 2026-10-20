package com.acme.salary.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.acme.salary.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

	@org.springframework.data.jpa.repository.Query("""
			select max(e.employeeCode) from Employee e where e.employeeCode like 'EMP%'
			""")
	String maxSeedCode();

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByEmployeeCodeIgnoreCase(String employeeCode);

	boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);

	boolean existsByEmployeeCodeIgnoreCaseAndIdNot(String employeeCode, Integer id);

	@Override
	@EntityGraph(attributePaths = { "country", "department", "salary" })
	Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

	@Override
	@EntityGraph(attributePaths = { "country", "department", "salary" })
	Optional<Employee> findById(Integer id);

	@Query("""
			select s.currencyCode as currencyCode, count(e.id) as headcount, sum(s.amount) as totalAmount
			from Employee e
			join e.salary s
			where e.employmentStatus <> com.acme.salary.domain.EmploymentStatus.TERMINATED
			group by s.currencyCode
			order by s.currencyCode
			""")
	List<CurrencyTotalView> payrollByCurrency();

	@Query("""
			select c.code as countryCode, c.name as countryName, s.currencyCode as currencyCode,
			       count(e.id) as headcount, sum(s.amount) as totalAmount
			from Employee e
			join e.country c
			join e.salary s
			where e.employmentStatus <> com.acme.salary.domain.EmploymentStatus.TERMINATED
			group by c.code, c.name, s.currencyCode
			order by c.name
			""")
	List<CountryPayView> payrollByCountry();

	@Query("""
			select d.id as departmentId, d.name as departmentName, s.currencyCode as currencyCode,
			       count(e.id) as headcount, sum(s.amount) as totalAmount
			from Employee e
			join e.department d
			join e.salary s
			where e.employmentStatus <> com.acme.salary.domain.EmploymentStatus.TERMINATED
			group by d.id, d.name, s.currencyCode
			order by d.name, s.currencyCode
			""")
	List<DepartmentPayView> payrollByDepartment();
}
