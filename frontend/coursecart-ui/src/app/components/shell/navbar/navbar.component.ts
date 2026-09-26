import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  
  isLoggedIn = false;
  isAdmin = false;
  userName = '';

  constructor(public authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.isAdmin = this.authService.isAdmin();
      this.userName = user ? user.name : '';
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
