import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../api.service';
import { AuthService } from '../auth.service';
import { Role } from '../models';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="auth-page">
      <div class="auth-card">
        <span class="eyebrow">JOIN REFERRALHUB</span>
        <h1>Create your account</h1>
        <p class="muted">Candidates complete their profile next. Employees can start posting opportunities.</p>
        <form (ngSubmit)="register()">
          <label>Full name</label>
          <input [(ngModel)]="name" name="name" required />
          <label>Email</label>
          <input type="email" [(ngModel)]="email" name="email" required />
          <label>Password</label>
          <input type="password" [(ngModel)]="password" name="password" minlength="6" required />
          <label>I am a</label>
          <div class="role-grid">
            <button type="button" [class.selected]="role === 'CANDIDATE'" (click)="role='CANDIDATE'">Candidate</button>
            <button type="button" [class.selected]="role === 'EMPLOYEE'" (click)="role='EMPLOYEE'">Employee</button>
          </div>
          <p class="error-message" *ngIf="error">{{ error }}</p>
          <button class="button primary full" [disabled]="loading">{{ loading ? 'Creating...' : 'Create Account' }}</button>
        </form>
        <p class="auth-switch">Already registered? <a routerLink="/login">Sign in</a></p>
      </div>
    </div>
  `
})
export class RegisterComponent {
  private api = inject(ApiService); private auth = inject(AuthService); private router = inject(Router);
  name=''; email=''; password=''; role: Role='CANDIDATE'; loading=false; error='';

  register() {
    this.loading=true; this.error='';
    this.api.register(this.name, this.email, this.password, this.role).subscribe({
      next: user => { this.auth.login(user); this.router.navigateByUrl(user.role === 'CANDIDATE' ? '/candidate/profile' : '/employee'); },
      error: err => { this.loading=false; this.error=err?.error?.message || 'Could not create the account.'; }
    });
  }
}
