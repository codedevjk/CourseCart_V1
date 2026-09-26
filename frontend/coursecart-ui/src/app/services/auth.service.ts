import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly USER_KEY = 'currentUser';
  
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;

  constructor() { 
    const storedUser = localStorage.getItem(this.USER_KEY);
    let initialUser: User | null = null;
    if (storedUser) {
      try {
        initialUser = JSON.parse(storedUser);
      } catch(e) {}
    }
    this.currentUserSubject = new BehaviorSubject<User | null>(initialUser);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  setCurrentUser(user: User): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  logout(): void {
    localStorage.removeItem(this.USER_KEY);
    this.currentUserSubject.next(null);
  }

  isLoggedIn(): boolean {
    return this.getCurrentUser() !== null;
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user !== null && user.role === 'ADMIN';
  }
}
