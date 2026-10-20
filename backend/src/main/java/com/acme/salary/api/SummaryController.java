package com.acme.salary.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.salary.api.dto.PaySummaryResponse;
import com.acme.salary.service.SummaryService;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

	private final SummaryService summaryService;

	public SummaryController(SummaryService summaryService) {
		this.summaryService = summaryService;
	}

	@GetMapping
	public PaySummaryResponse summarize() {
		return summaryService.summarize();
	}
}
