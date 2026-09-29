import { Routes } from '@angular/router';
import { authGuard, candidateGuard, employeeGuard } from './guards';
import { HomeComponent } from './pages/home.component';
import { JobDetailsComponent } from './pages/job-details.component';
import { EmployeeComponent } from './pages/employee.component';
import { LoginComponent } from './pages/login.component';
import { RegisterComponent } from './pages/register.component';
import { CandidateProfileComponent } from './pages/candidate-profile.component';

export const routes: Routes = [
  { path:'login', component:LoginComponent },
  { path:'register', component:RegisterComponent },
  { path:'candidate', component:HomeComponent, canActivate:[candidateGuard] },
  { path:'candidate/profile', component:CandidateProfileComponent, canActivate:[candidateGuard] },
  { path:'jobs/:id', component:JobDetailsComponent, canActivate:[candidateGuard] },
  { path:'employee', component:EmployeeComponent, canActivate:[employeeGuard] },
  { path:'', pathMatch:'full', redirectTo:'login' },
  { path:'**', redirectTo:'login' }
];
