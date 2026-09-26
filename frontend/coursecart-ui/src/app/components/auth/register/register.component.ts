import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  errorMessage = '';
  successMessage = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  // Helper getters for template
  get name() { return this.registerForm.get('name'); }
  get username() { return this.registerForm.get('username'); }
  get password() { return this.registerForm.get('password'); }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.errorMessage = 'Please fill out all fields correctly.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.userService.register(this.registerForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Registration successful! Redirecting to login...';
        // Seamless redirect to login upon success
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 409) {
          this.errorMessage = 'Username already exists. Please choose another one.';
        } else {
          this.errorMessage = 'An error occurred during registration. Please try again.';
        }
      }
    });
  }
}
