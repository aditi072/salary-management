import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { SalaryApi } from './salary-api';

describe('SalaryApi', () => {
  let api: SalaryApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(SalaryApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('sends only the filters the HR manager filled in', () => {
    api
      .employees({ q: 'Asha', country: 'IN', departmentId: '', status: 'ACTIVE', page: 1, size: 20 })
      .subscribe();

    const request = http.expectOne((call) => call.url === '/api/employees');
    expect(request.request.params.get('q')).toBe('Asha');
    expect(request.request.params.get('country')).toBe('IN');
    expect(request.request.params.get('status')).toBe('ACTIVE');
    expect(request.request.params.has('departmentId')).toBe(false);
    expect(request.request.params.get('page')).toBe('1');
    request.flush({ content: [], totalElements: 0, totalPages: 0, size: 20, number: 1 });
  });
});
