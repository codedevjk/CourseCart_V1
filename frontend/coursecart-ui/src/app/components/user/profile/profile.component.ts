import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { EnrollmentService } from '../../../services/enrollment.service';
import { User } from '../../../models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User | null = null;
  coursesOwned: number = 0;
  isLoading = true;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private enrollmentService: EnrollmentService
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.errorMessage = 'You must be logged in to view your profile.';
      this.isLoading = false;
      return;
    }

    // TODO[TRAINEE]: Fetch user profile from userService (US 03).
    // If the user's role is 'USER', fetch their enrollments to set coursesOwned.
    // Ensure isLoading is updated and errors are caught.
    throw new Error('TODO[TRAINEE]: Implement profile fetching');
  }
}
