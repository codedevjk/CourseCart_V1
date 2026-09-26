import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    userServiceSpy = jasmine.createSpyObj('UserService', ['register']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [FormsModule],
      providers: [
        { provide: UserService, useValue: userServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call register on submit and navigate to login on success', () => {
    (component.user as any) = { name: 'Test User', username: 'testuser', password: 'password', role: 'USER' };
    userServiceSpy.register.and.returnValue(of(component.user as any));

    component.onSubmit();

    expect(userServiceSpy.register).toHaveBeenCalledWith(component.user);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/courses']);
  });

  it('should set error message on registration failure', () => {
    (component.user as any) = { name: 'Test User', username: 'testuser', password: 'password', role: 'USER' };
    userServiceSpy.register.and.returnValue(throwError(() => new Error('Registration failed')));

    component.onSubmit();

    expect(component.errorMessage).toBe('An error occurred during registration. Please try again.');
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });
});
