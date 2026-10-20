import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import {
  Country,
  Department,
  Employee,
  EmployeePage,
  EmployeeQuery,
  EmployeeWrite,
  PaySummary,
} from './salary.models';

@Injectable({ providedIn: 'root' })
export class SalaryApi {
  private readonly http = inject(HttpClient);

  employees(query: EmployeeQuery): Observable<EmployeePage> {
    let params = new HttpParams().set('page', query.page).set('size', query.size);
    if (query.q.trim()) {
      params = params.set('q', query.q.trim());
    }
    if (query.country) {
      params = params.set('country', query.country);
    }
    if (query.departmentId) {
      params = params.set('departmentId', query.departmentId);
    }
    if (query.status) {
      params = params.set('status', query.status);
    }
    return this.http.get<EmployeePage>('/api/employees', { params });
  }

  employee(id: number): Observable<Employee> {
    return this.http.get<Employee>(`/api/employees/${id}`);
  }

  create(body: EmployeeWrite): Observable<Employee> {
    return this.http.post<Employee>('/api/employees', body);
  }

  update(id: number, body: EmployeeWrite): Observable<Employee> {
    return this.http.put<Employee>(`/api/employees/${id}`, body);
  }

  countries(): Observable<Country[]> {
    return this.http.get<Country[]>('/api/countries');
  }

  departments(): Observable<Department[]> {
    return this.http.get<Department[]>('/api/departments');
  }

  summary(): Observable<PaySummary> {
    return this.http.get<PaySummary>('/api/summary');
  }
}
