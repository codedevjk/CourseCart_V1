import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    userServiceSpy = jasmine.createSpyObj('UserService', ['login']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['setCurrentUser', 'isAdmin']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [FormsModule],
      providers: [
        { provide: UserService, useValue: userServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call login on submit and navigate to courses on USER role', () => {
    component.credentials = { username: 'test', password: 'password' };
    const mockUser = { id: 1, role: 'USER' };
    userServiceSpy.login.and.returnValue(of(mockUser as any));

    component.onSubmit();

    expect(userServiceSpy.login).toHaveBeenCalledWith(component.credentials);
    expect(authServiceSpy.setCurrentUser).toHaveBeenCalledWith(mockUser as any);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/courses']);
  });

  it('should call login on submit and navigate to admin on ADMIN role', () => {
    component.credentials = { username: 'admin', password: 'password' };
    const mockUser = { id: 2, role: 'ADMIN' };
    userServiceSpy.login.and.returnValue(of(mockUser as any));
    authServiceSpy.isAdmin.and.returnValue(true);

    component.onSubmit();

    expect(userServiceSpy.login).toHaveBeenCalledWith(component.credentials);
    expect(authServiceSpy.setCurrentUser).toHaveBeenCalledWith(mockUser as any);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/courses']);
  });

  it('should set error message on login failure', () => {
    component.credentials = { username: 'test', password: 'wrong' };
    userServiceSpy.login.and.returnValue(throwError(() => new Error('Invalid credentials')));

    component.onSubmit();

    expect(component.errorMessage).toBe('Invalid username or password. Please try again.');
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });
});
