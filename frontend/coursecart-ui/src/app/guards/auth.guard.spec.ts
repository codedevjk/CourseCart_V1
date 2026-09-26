import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['isLoggedIn']);
    routerSpy = jasmine.createSpyObj('Router', ['parseUrl']);

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });
    guard = TestBed.inject(AuthGuard);
  });

  it('should allow activation when user is logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(true);
    
    const result = guard.canActivate();
    
    expect(result).toBeTrue();
    expect(routerSpy.parseUrl).not.toHaveBeenCalled();
  });

  it('should redirect to /login when user is not logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(false);
    routerSpy.parseUrl.and.returnValue('mockUrlTree' as any);
    
    const result = guard.canActivate();
    
    expect(result).toBe('mockUrlTree' as any);
    expect(routerSpy.parseUrl).toHaveBeenCalledWith('/login');
  });
});
