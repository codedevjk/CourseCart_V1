import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean | UrlTree {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.role === 'ADMIN') {
      return true;
    }
    
    // Redirect to home if not admin (or to login if not logged in)
    if (!currentUser) {
      return this.router.parseUrl('/login');
    }
    return this.router.parseUrl('/');
  }
}
