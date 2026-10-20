import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';

import { formatMoney } from '../salary/money';
import { Country, Department, Employee } from '../salary/salary.models';
import { SalaryApi } from '../salary/salary-api';

@Component({
  selector: 'app-directory',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
  ],
  templateUrl: './directory.html',
})
export class Directory implements OnInit {
  private readonly api = inject(SalaryApi);
  private readonly formBuilder = inject(FormBuilder);

  readonly columns = ['employeeCode', 'name', 'country', 'department', 'jobTitle', 'status', 'salary'];
  readonly filters = this.formBuilder.nonNullable.group({
    q: '',
    country: '',
    departmentId: '',
    status: '',
  });

  readonly countries = signal<Country[]>([]);
  readonly departments = signal<Department[]>([]);
  readonly rows = signal<Employee[]>([]);
  readonly total = signal(0);
  readonly page = signal(0);
  readonly pageSize = signal(20);
  readonly error = signal('');

  ngOnInit(): void {
    this.api.countries().subscribe((countries) => this.countries.set(countries));
    this.api.departments().subscribe((departments) => this.departments.set(departments));
    this.load();
  }

  search(): void {
    this.page.set(0);
    this.load();
  }

  changePage(event: PageEvent): void {
    this.page.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.load();
  }

  money(employee: Employee): string {
    return formatMoney(employee.salaryAmount, employee.currencyCode);
  }

  private load(): void {
    const filters = this.filters.getRawValue();
    this.api
      .employees({
        ...filters,
        page: this.page(),
        size: this.pageSize(),
      })
      .subscribe({
        next: (page) => {
          this.rows.set(page.content);
          this.total.set(page.totalElements);
          this.error.set('');
        },
        error: () => this.error.set('The employee list could not be loaded. Is the API running on port 8080?'),
      });
  }
}
