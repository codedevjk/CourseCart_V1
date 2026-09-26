import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AdminGuard } from './admin.guard';
import { AuthService } from '../services/auth.service';

describe('AdminGuard', () => {
  let guard: AdminGuard;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser']);
    routerSpy = jasmine.createSpyObj('Router', ['parseUrl']);

    TestBed.configureTestingModule({
      providers: [
        AdminGuard,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });
    guard = TestBed.inject(AdminGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should allow navigation if user is ADMIN', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ role: 'ADMIN' } as any);
    expect(guard.canActivate()).toBeTrue();
  });

  it('should redirect to / if user is logged in but not ADMIN', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ role: 'USER' } as any);
    routerSpy.parseUrl.and.returnValue({} as any);
    guard.canActivate();
    expect(routerSpy.parseUrl).toHaveBeenCalledWith('/');
  });

  it('should redirect to /login if user is not logged in', () => {
    authServiceSpy.getCurrentUser.and.returnValue(null);
    routerSpy.parseUrl.and.returnValue({} as any);
    guard.canActivate();
    expect(routerSpy.parseUrl).toHaveBeenCalledWith('/login');
  });
});
