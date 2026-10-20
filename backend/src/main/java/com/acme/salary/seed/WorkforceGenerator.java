package com.acme.salary.seed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.acme.salary.domain.Country;
import com.acme.salary.domain.Department;
import com.acme.salary.domain.EmploymentStatus;

public final class WorkforceGenerator {

	static final LocalDate HIRE_FROM = LocalDate.of(2014, 1, 1);
	static final LocalDate HIRE_TO = LocalDate.of(2026, 6, 30);
	static final LocalDate TODAY = LocalDate.of(2026, 10, 20);

	private static final String[] FIRST_NAMES = {
			"Asha", "Dev", "Meera", "Arjun", "Priya", "Rohan", "Neha", "Vikram",
			"John", "Emma", "Liam", "Olivia", "Noah", "Ava", "Ethan", "Mia",
			"Hans", "Lena", "Jonas", "Sofia", "Felix", "Greta", "Omar", "Layla",
			"Wei", "Hui", "Amir", "Sara", "Noah", "Ines", "Kabir", "Ananya"
	};

	private static final String[] LAST_NAMES = {
			"Rao", "Shah", "Iyer", "Kapoor", "Nair", "Mehta", "Das", "Singh",
			"Doe", "Smith", "Brown", "Garcia", "Miller", "Wilson", "Taylor", "Anderson",
			"Keller", "Vogel", "Becker", "Hoffmann", "Wagner", "Schulz", "Khan", "Farouk",
			"Tan", "Lim", "Rahman", "Costa", "Silva", "Nowak", "Berg", "Patel"
	};

	private static final Map<String, String[]> TITLES = Map.of(
			"Engineering", new String[] { "Software Engineer", "Senior Software Engineer", "QA Engineer" },
			"Product", new String[] { "Product Manager", "Associate Product Manager" },
			"Design", new String[] { "Product Designer", "UX Researcher" },
			"People", new String[] { "HR Partner", "Recruiter" },
			"Finance", new String[] { "Accountant", "Financial Analyst" },
			"Sales", new String[] { "Account Executive", "Sales Manager" },
			"Marketing", new String[] { "Marketing Manager", "Content Specialist" },
			"Operations", new String[] { "Operations Manager", "Operations Analyst" },
			"Customer Support", new String[] { "Support Specialist", "Support Lead" },
			"Legal", new String[] { "Counsel", "Legal Associate" });

	private static final Map<String, int[]> ANNUAL_BAND = Map.of(
			"IN", new int[] { 400_000, 3_500_000 },
			"US", new int[] { 55_000, 180_000 },
			"GB", new int[] { 35_000, 120_000 },
			"DE", new int[] { 40_000, 110_000 },
			"SG", new int[] { 48_000, 150_000 },
			"AE", new int[] { 90_000, 280_000 });

	private WorkforceGenerator() {
	}

	public static List<NewHire> generate(int count, List<Country> countries, List<Department> departments) {
		return generate(count, 1, countries, departments);
	}

	public static List<NewHire> generate(int count, int firstNumber, List<Country> countries,
			List<Department> departments) {
		if (count <= 0) {
			throw new IllegalArgumentException("Employee count must be positive");
		}
		if (countries.isEmpty() || departments.isEmpty()) {
			throw new IllegalArgumentException("Countries and departments are required");
		}
		List<Country> orderedCountries = countries.stream()
				.sorted(Comparator.comparing(Country::getCode))
				.toList();
		List<Department> orderedDepartments = departments.stream()
				.sorted(Comparator.comparing(Department::getName))
				.toList();
		Random random = new Random(20_261_020L);
		int hireSpan = (int) ChronoUnit.DAYS.between(HIRE_FROM, HIRE_TO);

		return java.util.stream.IntStream.range(0, count)
				.mapToObj(offset -> hire(firstNumber + offset, random, hireSpan, orderedCountries, orderedDepartments))
				.toList();
	}

	private static NewHire hire(int number, Random random, int hireSpan, List<Country> countries,
			List<Department> departments) {
		String code = "EMP" + String.format("%05d", number);
		String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
		String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
		Country country = countries.get(random.nextInt(countries.size()));
		Department department = departments.get(random.nextInt(departments.size()));
		LocalDate hireDate = HIRE_FROM.plusDays(random.nextInt(hireSpan + 1));
		LocalDate effectiveFrom = hireDate.plusDays(random.nextInt(120));
		if (effectiveFrom.isAfter(TODAY)) {
			effectiveFrom = hireDate;
		}
		return new NewHire(
				code,
				firstName,
				lastName,
				code.toLowerCase() + "@acme.test",
				country,
				department,
				titleFor(department, random),
				status(random),
				hireDate,
				salary(country, random),
				country.getCurrencyCode(),
				effectiveFrom);
	}

	private static String titleFor(Department department, Random random) {
		String[] titles = TITLES.getOrDefault(department.getName(), new String[] { "Specialist" });
		return titles[random.nextInt(titles.length)];
	}

	private static EmploymentStatus status(Random random) {
		int roll = random.nextInt(100);
		if (roll < 5) {
			return EmploymentStatus.TERMINATED;
		}
		if (roll < 12) {
			return EmploymentStatus.ON_LEAVE;
		}
		return EmploymentStatus.ACTIVE;
	}

	private static BigDecimal salary(Country country, Random random) {
		int[] band = ANNUAL_BAND.get(country.getCode());
		if (band == null) {
			throw new IllegalArgumentException("No salary band for country " + country.getCode());
		}
		int amount = band[0] + random.nextInt(band[1] - band[0] + 1);
		return BigDecimal.valueOf(amount);
	}
}
