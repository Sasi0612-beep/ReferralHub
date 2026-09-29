import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';
import { AuthService } from '../auth.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="section-head">
      <div><span class="eyebrow">CANDIDATE PROFILE</span><h1>Tell employees what you bring.</h1><p class="muted">Your profile powers the job match and gives the referral agent useful context.</p></div>
    </section>
    <form class="form-panel" (ngSubmit)="save()">
      <div class="form-grid">
        <div><label>Skills</label><textarea [(ngModel)]="profile.skills" name="skills" rows="4" placeholder="Java, Spring Boot, SQL, Angular..."></textarea></div>
        <div><label>Experience</label><textarea [(ngModel)]="profile.experience" name="experience" rows="4" placeholder="Internships, roles, responsibilities..."></textarea></div>
        <div><label>Projects</label><textarea [(ngModel)]="profile.projects" name="projects" rows="4"></textarea></div>
        <div><label>Preferred roles</label><input [(ngModel)]="profile.preferredRoles" name="preferredRoles" /></div>
        <div><label>Preferred locations</label><input [(ngModel)]="profile.preferredLocations" name="preferredLocations" /></div>
        <div><label>Graduation year</label><input type="number" [(ngModel)]="profile.graduationYear" name="graduationYear" /></div>
        <div><label>Phone</label><input [(ngModel)]="profile.phone" name="phone" /></div>
        <div><label>Current location</label><input [(ngModel)]="profile.currentLocation" name="currentLocation" /></div>
      </div>
      <p class="success-text" *ngIf="message">{{ message }}</p><p class="error-message" *ngIf="error">{{ error }}</p>
      <div class="actions"><button type="submit" class="button primary" [disabled]="saving">{{ saving ? 'Saving...' : 'Save Profile' }}</button><button type="button" class="button secondary" (click)="router.navigate(['/candidate'])">Back to Jobs</button></div>
    </form>
  `
})
export class CandidateProfileComponent implements OnInit {
  private api=inject(ApiService); private auth=inject(AuthService); router=inject(Router);
  saving=false; message=''; error='';
  profile:any={skills:'',experience:'',projects:'',preferredRoles:'',preferredLocations:'',graduationYear:2026,phone:'',currentLocation:''};
  ngOnInit(){ const user=this.auth.getCurrentUser(); if(!user){this.router.navigate(['/login']);return;} this.api.getCandidateProfile(user.id).subscribe({next:p=>{this.profile={...this.profile,...p};},error:()=>{}}); }
  save(){ const user=this.auth.getCurrentUser(); if(!user)return; this.saving=true;this.message='';this.error='';this.api.saveCandidateProfile(user.id,this.profile).subscribe({next:p=>{this.profile={...this.profile,...p};this.saving=false;this.message='Profile saved successfully.';},error:e=>{this.saving=false;this.error=e?.error?.message||'Could not save profile.';}}); }
}
