# Apply the schema

Design notes are in [docs/database.md](../docs/database.md).

In MySQL Workbench, connect to the local MySQL 8 server and run:

```sql
SOURCE db/schema.sql;
SOURCE db/reference-data.sql;
```

Or from this folder:

```powershell
mysql -u root -p -e "SOURCE db/schema.sql; SOURCE db/reference-data.sql;"
```

That creates `salary_management`, the tables, the `employee_compensation` view, and the country and department rows. The 10,000 employees are loaded by a later seed script.
