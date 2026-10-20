import { Routes } from '@angular/router';

import { Directory } from './directory/directory';
import { EmployeeForm } from './employee-form/employee-form';
import { Summary } from './summary/summary';

export const routes: Routes = [
  { path: '', component: Directory },
  { path: 'summary', component: Summary },
  { path: 'employees/new', component: EmployeeForm },
  { path: 'employees/:id', component: EmployeeForm },
];
