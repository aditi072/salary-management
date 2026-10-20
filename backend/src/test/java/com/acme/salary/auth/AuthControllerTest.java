package com.acme.salary.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void directoryRequiresSignIn() throws Exception {
		mockMvc.perform(get("/api/employees")).andExpect(status().isUnauthorized());
	}

	@Test
	void wrongPasswordIsRejected() throws Exception {
		mockMvc.perform(post("/api/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"hr\",\"password\":\"nope\"}"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void signInOpensTheDirectory() throws Exception {
		MvcResult login = mockMvc.perform(post("/api/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"hr\",\"password\":\"hr-demo\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("hr"))
				.andReturn();

		MockHttpSession session = (MockHttpSession) login.getRequest().getSession();
		mockMvc.perform(get("/api/me").session(session))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("hr"));
	}

	@Test
	void loginAcceptsTheCsrfCookieAngularSends() throws Exception {
		MvcResult prime = mockMvc.perform(get("/api/me")).andReturn();
		Cookie cookie = prime.getResponse().getCookie("XSRF-TOKEN");
		assertNotNull(cookie);

		mockMvc.perform(post("/api/login")
				.cookie(cookie)
				.header("X-XSRF-TOKEN", cookie.getValue())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"hr\",\"password\":\"hr-demo\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("hr"));
	}
}
