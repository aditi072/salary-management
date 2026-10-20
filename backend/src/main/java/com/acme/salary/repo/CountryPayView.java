package com.acme.salary.repo;

import java.math.BigDecimal;

public interface CountryPayView {

	String getCountryCode();

	String getCountryName();

	String getCurrencyCode();

	long getHeadcount();

	BigDecimal getTotalAmount();
}
