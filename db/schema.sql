-- ACME salary management
-- MySQL 8. One current salary per employee. Amounts are stored in the
-- employee's local currency and must not be summed across currencies.

CREATE DATABASE IF NOT EXISTS salary_management
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE salary_management;

CREATE TABLE countries (
  code CHAR(2) NOT NULL,
  name VARCHAR(80) NOT NULL,
  currency_code CHAR(3) NOT NULL,
  PRIMARY KEY (code),
  UNIQUE KEY uk_countries_name (name)
) ENGINE=InnoDB;

CREATE TABLE departments (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_departments_name (name)
) ENGINE=InnoDB;

CREATE TABLE employees (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  employee_code VARCHAR(16) NOT NULL,
  first_name VARCHAR(60) NOT NULL,
  last_name VARCHAR(60) NOT NULL,
  email VARCHAR(120) NOT NULL,
  country_code CHAR(2) NOT NULL,
  department_id SMALLINT UNSIGNED NOT NULL,
  job_title VARCHAR(80) NOT NULL,
  employment_status ENUM('ACTIVE', 'ON_LEAVE', 'TERMINATED') NOT NULL DEFAULT 'ACTIVE',
  hire_date DATE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_employees_code (employee_code),
  UNIQUE KEY uk_employees_email (email),
  KEY idx_employees_country (country_code),
  KEY idx_employees_department (department_id),
  KEY idx_employees_status (employment_status),
  KEY idx_employees_name (last_name, first_name),
  CONSTRAINT fk_employees_country
    FOREIGN KEY (country_code) REFERENCES countries (code),
  CONSTRAINT fk_employees_department
    FOREIGN KEY (department_id) REFERENCES departments (id)
) ENGINE=InnoDB;

CREATE TABLE salaries (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  employee_id INT UNSIGNED NOT NULL,
  amount DECIMAL(12, 2) NOT NULL,
  currency_code CHAR(3) NOT NULL,
  effective_from DATE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_salaries_employee (employee_id),
  KEY idx_salaries_currency_amount (currency_code, amount),
  CONSTRAINT fk_salaries_employee
    FOREIGN KEY (employee_id) REFERENCES employees (id),
  CONSTRAINT chk_salaries_amount_positive CHECK (amount > 0)
) ENGINE=InnoDB;

-- Read model for the directory and dashboard. One row per employee.
CREATE VIEW employee_compensation AS
SELECT
  e.id,
  e.employee_code,
  e.first_name,
  e.last_name,
  e.email,
  e.country_code,
  c.name AS country_name,
  e.department_id,
  d.name AS department_name,
  e.job_title,
  e.employment_status,
  e.hire_date,
  s.amount AS salary_amount,
  s.currency_code,
  s.effective_from
FROM employees e
JOIN countries c ON c.code = e.country_code
JOIN departments d ON d.id = e.department_id
JOIN salaries s ON s.employee_id = e.id;

INSERT INTO countries (code, name, currency_code) VALUES
  ('IN', 'India', 'INR'),
  ('US', 'United States', 'USD'),
  ('GB', 'United Kingdom', 'GBP'),
  ('DE', 'Germany', 'EUR'),
  ('SG', 'Singapore', 'SGD'),
  ('AE', 'United Arab Emirates', 'AED');

INSERT INTO departments (name) VALUES
  ('Engineering'),
  ('Product'),
  ('Design'),
  ('People'),
  ('Finance'),
  ('Sales'),
  ('Marketing'),
  ('Operations'),
  ('Customer Support'),
  ('Legal');
