import { Routes } from '@angular/router';

import { authGuard } from './auth/auth.guard';
import { Directory } from './directory/directory';
import { EmployeeForm } from './employee-form/employee-form';
import { Login } from './login/login';
import { Summary } from './summary/summary';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: '', component: Directory, canActivate: [authGuard] },
  { path: 'summary', component: Summary, canActivate: [authGuard] },
  { path: 'employees/new', component: EmployeeForm, canActivate: [authGuard] },
  { path: 'employees/:id', component: EmployeeForm, canActivate: [authGuard] },
];
