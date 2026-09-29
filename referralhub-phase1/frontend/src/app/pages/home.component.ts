import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApiService } from '../api.service';
import { Job } from '../models';

@Component({ standalone:true, imports:[CommonModule,RouterLink], template:`
<section class="hero"><div><span class="eyebrow">REFERRAL OPPORTUNITIES</span><h1>Find jobs through people who can refer you.</h1><p>Browse newly posted opportunities, check your skill match and request a referral with context.</p></div><div class="hero-stat"><strong>{{jobs.length}}</strong><span>latest opportunities</span></div></section>
<section class="section-head"><div><span class="eyebrow">LIVE FEED</span><h2>Latest Referral Opportunities</h2></div><span class="pill">New jobs appear here automatically</span></section>
<div class="job-grid"><article class="job-card" *ngFor="let job of jobs"><div class="job-top"><div class="company-logo">{{job.company.name.charAt(0)}}</div><span class="new-badge">NEW</span></div><h3>{{job.title}}</h3><p class="company">{{job.company.name}} · {{job.location}}</p><p class="muted">{{job.description}}</p><div class="skills"><span *ngFor="let skill of split(job.requiredSkills)">{{skill}}</span></div><div class="job-footer"><small>{{job.experience}}</small><a [routerLink]="['/jobs',job.id]" class="button primary">View Job</a></div></article></div>
<div class="empty panel" *ngIf="!jobs.length">No active jobs yet.</div>` })
export class HomeComponent implements OnInit { private api=inject(ApiService); jobs:Job[]=[]; ngOnInit(){this.api.latestJobs().subscribe({next:d=>this.jobs=d});} split(v:string){return (v||'').split(',').map(s=>s.trim()).filter(Boolean).slice(0,5);} }
