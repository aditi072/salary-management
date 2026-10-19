-- Lookup rows the HR filters depend on. Employee rows come from the seed script.

USE salary_management;

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
