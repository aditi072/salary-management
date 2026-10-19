# Database

MySQL 8, database name `salary_management`. Chosen because it is a relational store the HR data model fits, and because it can be inspected in MySQL Workbench. Postgres would also work; we did not need it.

## Tables

- `countries` — country and the currency salaries in that country use.
- `departments` — stable names for filters and the pay-by-department summary.
- `employees` — the person and where they sit in the org.
- `salaries` — **one current salary per employee** (`employee_id` is unique). Amount must be greater than zero.

`employee_compensation` joins those tables for the directory and dashboard. It is a view, not a place to write.

## Why salary is its own table

The employee row is identity and org placement. Pay is a separate fact: amount, currency, and the date it became effective. Version one still stores only the current row. A later history feature can drop the unique constraint and add rows without reshaping `employees`.

## What we index

Filters and search hit country, department, status, and last name + first name. Pay summaries group by `currency_code` and aggregate `amount`, so that pair is indexed together.

## Currency rule

`salaries.currency_code` is stored on the salary row, not only inferred at read time. Summaries group by that code. A single “total payroll” across INR and USD is intentionally not provided.

## Apply locally

From this folder, in MySQL Workbench or the `mysql` client:

```sql
SOURCE db/schema.sql;
SOURCE db/reference-data.sql;
```

The 10,000-employee seed script is a later step. It will insert into `employees` and `salaries` only.
