package com.acme.salary.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.seed", havingValue = "true")
public class SeedRunner implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SeedRunner.class);

	private final SeedService seedService;
	private final ApplicationContext applicationContext;

	public SeedRunner(SeedService seedService, ApplicationContext applicationContext) {
		this.seedService = seedService;
		this.applicationContext = applicationContext;
	}

	@Override
	public void run(String... args) {
		int created = seedService.seedUpTo(10_000);
		if (created == 0) {
			log.info("Employee directory already has at least 10000 people, so the seed did nothing");
		}
		else {
			log.info("Inserted {} employees with current salaries", created);
		}
		int exitCode = SpringApplication.exit(applicationContext, () -> 0);
		System.exit(exitCode);
	}
}
