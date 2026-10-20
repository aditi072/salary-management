import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { Country, Department, EmploymentStatus } from '../salary/salary.models';
import { SalaryApi } from '../salary/salary-api';

@Component({
  selector: 'app-employee-form',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
  ],
  templateUrl: './employee-form.html',
})
export class EmployeeForm implements OnInit {
  private readonly api = inject(SalaryApi);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly formBuilder = inject(FormBuilder);

  readonly countries = signal<Country[]>([]);
  readonly departments = signal<Department[]>([]);
  readonly error = signal('');
  readonly editing = signal(false);
  private employeeId: number | null = null;

  readonly form = this.formBuilder.nonNullable.group({
    employeeCode: ['', Validators.required],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    countryCode: ['', Validators.required],
    departmentId: [0, [Validators.required, Validators.min(1)]],
    jobTitle: ['', Validators.required],
    employmentStatus: ['ACTIVE' as EmploymentStatus, Validators.required],
    hireDate: ['', Validators.required],
    salaryAmount: [0, [Validators.required, Validators.min(0.01)]],
    effectiveFrom: [''],
  });

  ngOnInit(): void {
    this.api.countries().subscribe((countries) => this.countries.set(countries));
    this.api.departments().subscribe((departments) => this.departments.set(departments));
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      return;
    }
    this.editing.set(true);
    this.employeeId = Number(id);
    this.api.employee(this.employeeId).subscribe({
      next: (employee) => {
        this.form.patchValue({
          ...employee,
          effectiveFrom: employee.effectiveFrom,
        });
      },
      error: () => this.error.set('That employee could not be loaded.'),
    });
  }

  currencyHint(): string {
    const country = this.countries().find((item) => item.code === this.form.controls.countryCode.value);
    return country ? `Salary is stored in ${country.currencyCode}` : 'Salary uses the currency of the selected country';
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const raw = this.form.getRawValue();
    const body = {
      ...raw,
      departmentId: Number(raw.departmentId),
      salaryAmount: Number(raw.salaryAmount),
      effectiveFrom: raw.effectiveFrom || null,
    };
    const request = this.employeeId === null ? this.api.create(body) : this.api.update(this.employeeId, body);
    request.subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: (error) => this.error.set(error?.error?.error ?? 'The employee could not be saved.'),
    });
  }
}
