package com.acme.salary.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class PayrollMathTest {

	@Test
	void averagesMoneyToTwoDecimalPlaces() {
		assertThat(PayrollMath.average(new BigDecimal("3000.00"), 2)).isEqualByComparingTo("1500.00");
		assertThat(PayrollMath.average(new BigDecimal("1000.00"), 3)).isEqualByComparingTo("333.33");
	}

	@Test
	void refusesAnEmptyGroup() {
		assertThatThrownBy(() -> PayrollMath.average(new BigDecimal("10.00"), 0))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
