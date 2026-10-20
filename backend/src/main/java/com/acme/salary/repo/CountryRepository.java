package com.acme.salary.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.salary.domain.Country;

public interface CountryRepository extends JpaRepository<Country, String> {

	List<Country> findAllByOrderByNameAsc();
}
