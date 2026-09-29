import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../api.service';
import { AuthService } from '../auth.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="auth-page">
      <div class="auth-card">
        <span class="eyebrow">REFERRALHUB</span>
        <h1>Welcome back</h1>
        <p class="muted">Sign in to discover referral opportunities or manage your employee referrals.</p>
        <form (ngSubmit)="login()">
          <label>Email</label>
          <input type="email" [(ngModel)]="email" name="email" required />
          <label>Password</label>
          <input type="password" [(ngModel)]="password" name="password" required />
          <p class="error-message" *ngIf="error">{{ error }}</p>
          <button class="button primary full" [disabled]="loading">{{ loading ? 'Signing in...' : 'Sign In' }}</button>
        </form>
        <div class="demo-box">
          <strong>Demo accounts</strong>
          <div>Candidate: rahul@referralhub.demo / candidate123</div>
          <div>Employee: ananya@referralhub.demo / employee123</div>
        </div>
        <p class="auth-switch">New here? <a routerLink="/register">Create an account</a></p>
      </div>
    </div>
  `
})
export class LoginComponent {
  private api = inject(ApiService); private auth = inject(AuthService); private router = inject(Router);
  email = ''; password = ''; loading = false; error = '';

  login() {
    this.loading = true; this.error = '';
    this.api.login(this.email, this.password).subscribe({
      next: user => { this.auth.login(user); this.router.navigateByUrl(user.role === 'CANDIDATE' ? '/candidate' : '/employee'); },
      error: err => { this.loading = false; this.error = err?.error?.message || 'Invalid email or password.'; }
    });
  }
}
