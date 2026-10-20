package com.acme.salary.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.acme.salary.api.dto.EmployeeResponse;
import com.acme.salary.domain.EmploymentStatus;
import com.acme.salary.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private EmployeeService employeeService;

	@Test
	void listReturnsThePageFromTheService() throws Exception {
		when(employeeService.list(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(sample())));

		mockMvc.perform(get("/api/employees").param("country", "IN"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].employeeCode").value("E-1"))
				.andExpect(jsonPath("$.content[0].currencyCode").value("INR"));
	}

	@Test
	void createRejectsANonPositiveSalaryBeforeTheServiceRuns() throws Exception {
		mockMvc.perform(post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "employeeCode": "E-1",
						  "firstName": "Asha",
						  "lastName": "Rao",
						  "email": "asha@acme.test",
						  "countryCode": "IN",
						  "departmentId": 1,
						  "jobTitle": "Engineer",
						  "employmentStatus": "ACTIVE",
						  "hireDate": "2022-04-01",
						  "salaryAmount": 0
						}
						"""))
				.andExpect(status().isBadRequest());

		verify(employeeService, never()).create(any());
	}

	private EmployeeResponse sample() {
		return new EmployeeResponse(
				1,
				"E-1",
				"Asha",
				"Rao",
				"asha@acme.test",
				"IN",
				"India",
				(short) 1,
				"Engineering",
				"Engineer",
				EmploymentStatus.ACTIVE,
				LocalDate.of(2022, 4, 1),
				new BigDecimal("1800000.00"),
				"INR",
				LocalDate.of(2024, 1, 1));
	}
}
