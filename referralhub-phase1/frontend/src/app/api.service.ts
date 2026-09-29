import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Company, CandidateProfile, Job, ReferralAnalysis, ReferralRequest, Role, User } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api';

  latestJobs() { return this.http.get<Job[]>(`${this.base}/jobs/latest`); }
  job(id: number) { return this.http.get<Job>(`${this.base}/jobs/${id}`); }
  companies() { return this.http.get<Company[]>(`${this.base}/companies`); }

  login(email: string, password: string) {
    return this.http.post<User>(`${this.base}/auth/login`, { email, password });
  }

  register(name: string, email: string, password: string, role: Role) {
    return this.http.post<User>(`${this.base}/auth/register`, { name, email, password, role });
  }

  getCandidateProfile(candidateId: number) {
    return this.http.get<CandidateProfile>(`${this.base}/candidates/${candidateId}/profile`);
  }

  saveCandidateProfile(candidateId: number, profile: Partial<CandidateProfile>) {
    return this.http.post<CandidateProfile>(`${this.base}/candidates/${candidateId}/profile`, profile);
  }

  createCompany(company: Partial<Company>) {
    return this.http.post<Company>(`${this.base}/companies`, company);
  }

  createJob(job: Partial<Job>, companyId: number, employeeId: number) {
    return this.http.post<Job>(`${this.base}/jobs?companyId=${companyId}&employeeId=${employeeId}`, job);
  }

  employeeRequests(employeeId: number) {
    return this.http.get<ReferralRequest[]>(`${this.base}/referrals/employee/${employeeId}`);
  }

  analysis(jobId: number, candidateId: number) {
    return this.http.get<ReferralAnalysis>(`${this.base}/referrals/analysis?jobId=${jobId}&candidateId=${candidateId}`);
  }

  requestReferral(jobId: number, candidateId: number) {
    return this.http.post<ReferralRequest>(`${this.base}/referrals?jobId=${jobId}&candidateId=${candidateId}`, {});
  }

  decide(referralId: number, status: 'REFERRED' | 'DECLINED') {
    return this.http.patch<ReferralRequest>(`${this.base}/referrals/${referralId}/decision?status=${status}`, {});
  }
}
