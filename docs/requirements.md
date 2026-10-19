# Salary Management — Requirements

## Goal

Give ACME’s HR manager a web application to manage current salary data for 10,000 employees across countries, and to answer basic questions about how the organization pays people. This replaces day-to-day Excel use for finding people, correcting pay, and summarizing payroll.

## User

One HR manager. Employees do not sign in. This version has a single role.

## In scope

- An employee directory with the current salary: employee code, name, email, country, department, job title, employment status, hire date, salary amount, currency, and the date that salary took effect.
- Search by name or employee code.
- Filter by country, department, employment status, and salary range.
- A paged list, so 10,000 rows stay usable.
- Create an employee record and update that employee’s current salary.
- A dashboard grouped by currency: headcount, total payroll, average salary, and breakdowns by country and by department.
- A seed of 10,000 employees across a small set of countries and departments.

## Out of scope

- Payroll runs, tax, bonuses, deductions, and payslips.
- Salary history. Each employee has one current salary.
- Currency conversion. Amounts stay in the local currency, and totals are never added across currencies.
- Excel import and export. A seed script loads the initial data.
- Employee self-service, approval workflows, and extra roles.
- An audit trail of who changed a salary.

## Why this cut

The current pain is managing and questioning pay data, not running payroll. History, tax, and foreign-exchange conversion would add rules without answering the HR manager’s first questions. Those can be added later without replacing the employee record.

## Done when

The HR manager can find a person, correct their current salary, and see how pay is distributed by country and department for 10,000 employees.
