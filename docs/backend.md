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

From `backend`, copy `application-local.properties.example` to `application-local.properties` and put the Workbench password in the copy. That copy is ignored by git. The username defaults to `root`. Override it with `MYSQL_USER` if Workbench uses a different account.

```powershell
.\mvnw.cmd spring-boot:run
```

## Seed

With the country and department rows already loaded, insert 10,000 employees and their current salaries:

Copy `application-local.properties.example` to `application-local.properties` and put the Workbench password there. That file stays on this machine.

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=seed"
```

The command exits when the insert finishes. It adds people until the directory holds 10,000, and leaves anyone already there in place. A later run does nothing once that number is reached. Seeded codes run from `EMP00001` upward. Currency always matches the employee's country. About 5% are terminated and are left out of pay totals.
