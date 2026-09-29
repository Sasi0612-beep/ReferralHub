import { Injectable, signal } from '@angular/core';
import { User } from './models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSignal = signal<User | null>(this.loadUser());
  currentUser = this.currentUserSignal.asReadonly();

  login(user: User) {
    localStorage.setItem('referralhub_user', JSON.stringify(user));
    this.currentUserSignal.set(user);
  }

  logout() {
    localStorage.removeItem('referralhub_user');
    this.currentUserSignal.set(null);
  }

  getCurrentUser(): User | null { return this.currentUserSignal(); }
  isLoggedIn(): boolean { return this.currentUserSignal() !== null; }
  isCandidate(): boolean { return this.currentUserSignal()?.role === 'CANDIDATE'; }
  isEmployee(): boolean { return this.currentUserSignal()?.role === 'EMPLOYEE'; }

  private loadUser(): User | null {
    const stored = localStorage.getItem('referralhub_user');
    if (!stored) return null;
    try { return JSON.parse(stored) as User; }
    catch { localStorage.removeItem('referralhub_user'); return null; }
  }
}
