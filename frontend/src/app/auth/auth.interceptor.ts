import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthSession } from './auth-session';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const router = inject(Router);
  const session = inject(AuthSession);
  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !request.url.includes('/api/login') && !request.url.includes('/api/me')) {
        session.clear();
        router.navigateByUrl('/login');
      }
      return throwError(() => error);
    }),
  );
};
