import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './auth.service';

@Component({ selector:'app-root', standalone:true, imports:[CommonModule,RouterLink,RouterOutlet], template:`
<header class="topbar"><a routerLink="/" class="brand"><span class="brand-mark">R</span>ReferralHub</a><nav *ngIf="auth.isLoggedIn()"><ng-container *ngIf="auth.isCandidate()"><a routerLink="/candidate">Jobs</a><a routerLink="/candidate/profile">My Profile</a></ng-container><a *ngIf="auth.isEmployee()" routerLink="/employee">Employee Dashboard</a><span class="user-name">{{auth.getCurrentUser()?.name}}</span><button class="logout" (click)="logout()">Logout</button></nav><nav *ngIf="!auth.isLoggedIn()"><a routerLink="/login">Sign in</a><a routerLink="/register">Register</a></nav></header><main class="shell"><router-outlet /></main>` })
export class AppComponent { auth=inject(AuthService); logout(){this.auth.logout();location.href='/login';} }
