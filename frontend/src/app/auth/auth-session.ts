import { Injectable, inject, signal } from '@angular/core';
import { Observable, catchError, map, of, tap } from 'rxjs';

import { SalaryApi } from '../salary/salary-api';

@Injectable({ providedIn: 'root' })
export class AuthSession {
  private readonly api = inject(SalaryApi);
  readonly username = signal<string | null>(null);

  restore(): Observable<boolean> {
    return this.api.me().pipe(
      tap((me) => this.username.set(me.username)),
      map(() => true),
      catchError(() => {
        this.username.set(null);
        return of(false);
      }),
    );
  }

  login(username: string, password: string): Observable<void> {
    return this.api.login(username, password).pipe(
      tap((me) => this.username.set(me.username)),
      map(() => undefined),
    );
  }

  logout(): Observable<void> {
    return this.api.logout().pipe(tap(() => this.username.set(null)));
  }

  clear(): void {
    this.username.set(null);
  }
}
