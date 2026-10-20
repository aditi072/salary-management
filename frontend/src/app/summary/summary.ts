import { Component, inject, OnInit, signal } from '@angular/core';
import { MatTableModule } from '@angular/material/table';

import { formatMoney } from '../salary/money';
import { PaySummary } from '../salary/salary.models';
import { SalaryApi } from '../salary/salary-api';

@Component({
  selector: 'app-summary',
  imports: [MatTableModule],
  templateUrl: './summary.html',
})
export class Summary implements OnInit {
  private readonly api = inject(SalaryApi);

  readonly summary = signal<PaySummary | null>(null);
  readonly error = signal('');
  readonly countryColumns = ['countryName', 'currencyCode', 'headcount', 'totalAmount', 'averageAmount'];
  readonly departmentColumns = ['departmentName', 'currencyCode', 'headcount', 'totalAmount', 'averageAmount'];

  ngOnInit(): void {
    this.api.summary().subscribe({
      next: (summary) => this.summary.set(summary),
      error: () => this.error.set('The pay summary could not be loaded. Is the API running on port 8080?'),
    });
  }

  money(amount: number, currency: string): string {
    return formatMoney(amount, currency);
  }
}
