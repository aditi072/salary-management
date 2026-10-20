export type EmploymentStatus = 'ACTIVE' | 'ON_LEAVE' | 'TERMINATED';

export interface Employee {
  id: number;
  employeeCode: string;
  firstName: string;
  lastName: string;
  email: string;
  countryCode: string;
  countryName: string;
  departmentId: number;
  departmentName: string;
  jobTitle: string;
  employmentStatus: EmploymentStatus;
  hireDate: string;
  salaryAmount: number;
  currencyCode: string;
  effectiveFrom: string;
}

export interface EmployeePage {
  content: Employee[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface Country {
  code: string;
  name: string;
  currencyCode: string;
}

export interface Department {
  id: number;
  name: string;
}

export interface EmployeeWrite {
  employeeCode: string;
  firstName: string;
  lastName: string;
  email: string;
  countryCode: string;
  departmentId: number;
  jobTitle: string;
  employmentStatus: EmploymentStatus;
  hireDate: string;
  salaryAmount: number;
  effectiveFrom: string | null;
}

export interface CurrencyPayroll {
  currencyCode: string;
  headcount: number;
  totalAmount: number;
  averageAmount: number;
}

export interface CountryPayroll extends CurrencyPayroll {
  countryCode: string;
  countryName: string;
}

export interface DepartmentPayroll extends CurrencyPayroll {
  departmentId: number;
  departmentName: string;
}

export interface PaySummary {
  paidHeadcount: number;
  byCurrency: CurrencyPayroll[];
  byCountry: CountryPayroll[];
  byDepartment: DepartmentPayroll[];
}

export interface EmployeeQuery {
  q: string;
  country: string;
  departmentId: string;
  status: string;
  page: number;
  size: number;
}
