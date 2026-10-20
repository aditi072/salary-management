package com.acme.salary.repo;

import java.math.BigDecimal;

public interface DepartmentPayView {

	Short getDepartmentId();

	String getDepartmentName();

	String getCurrencyCode();

	long getHeadcount();

	BigDecimal getTotalAmount();
}
