package com.acme.salary.repo;

import java.math.BigDecimal;

public interface CurrencyTotalView {

	String getCurrencyCode();

	long getHeadcount();

	BigDecimal getTotalAmount();
}
