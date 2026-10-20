package com.acme.salary.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PayrollMath {

	private PayrollMath() {
	}

	public static BigDecimal average(BigDecimal total, long headcount) {
		if (headcount <= 0) {
			throw new IllegalArgumentException("Headcount must be positive");
		}
		return total.divide(BigDecimal.valueOf(headcount), 2, RoundingMode.HALF_UP);
	}
}
