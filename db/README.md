# Database

MySQL 8, the same server used with MySQL Workbench. The schema is the system of record for current employee pay.

## Tables

- `countries` — country and the currency salaries in that country are paid in.
- `departments` — fixed list, so filters stay consistent across 10,000 employees.
- `employees` — the person and where they work.
- `salaries` — one current salary per employee (`employee_id` is unique). No history table in this version.
- `employee_compensation` — a view that joins those tables for the directory and dashboard.

Salary totals are only meaningful inside one `currency_code`. Do not add INR and USD together.

## Apply it in MySQL Workbench

1. Open MySQL Workbench and connect to the local MySQL 8 instance.
2. Open `db/schema.sql`.
3. Execute the script.

That creates the `salary_management` database, the tables, the view, and the country and department reference rows. Employee rows come from the seed script in a later step.

## Apply it from the terminal

```powershell
mysql -u root -p < db/schema.sql
```
