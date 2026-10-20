# Backend

Java 21, Spring Boot, MySQL. The API is the desk the HR manager will use. The Angular screens come next and will call these addresses.

The tables already exist. The application does not create or alter them (`spring.jpa.hibernate.ddl-auto=none`).

## Addresses

- `GET /api/employees` — paged directory. Optional `q`, `country`, `departmentId`, `status`, `page`, `size` (size is capped at 100).
- `GET /api/employees/{id}`
- `POST /api/employees` and `PUT /api/employees/{id}` — write the person and their current salary. Currency is copied from the country. The client cannot pick a different currency.
- `GET /api/countries` and `GET /api/departments` — values for the filters.
- `GET /api/summary` — headcount and pay for people who are not terminated. Totals and averages are split by currency, then by country and by department.

A salary of zero or less is rejected. A duplicate email or employee code is rejected.

## Run

Set the MySQL password, then start the API from `backend`:

```powershell
$env:MYSQL_PASSWORD = "the-password-you-type-in-Workbench"
.\mvnw.cmd spring-boot:run
```

The username defaults to `root`. Override it with `MYSQL_USER` if Workbench uses a different account.
