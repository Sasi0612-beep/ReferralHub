import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService); const router = inject(Router);
  return auth.isLoggedIn() ? true : router.createUrlTree(['/login']);
};

export const candidateGuard: CanActivateFn = () => {
  const auth = inject(AuthService); const router = inject(Router);
  return auth.isCandidate() ? true : router.createUrlTree(['/login']);
};

export const employeeGuard: CanActivateFn = () => {
  const auth = inject(AuthService); const router = inject(Router);
  return auth.isEmployee() ? true : router.createUrlTree(['/login']);
};
