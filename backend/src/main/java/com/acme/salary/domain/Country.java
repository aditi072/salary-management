package com.acme.salary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {

	@Id
	@Column(length = 2)
	private String code;

	@Column(nullable = false, length = 80)
	private String name;

	@Column(name = "currency_code", nullable = false, length = 3)
	private String currencyCode;

	protected Country() {
	}

	public Country(String code, String name, String currencyCode) {
		this.code = code;
		this.name = name;
		this.currencyCode = currencyCode;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}
}
