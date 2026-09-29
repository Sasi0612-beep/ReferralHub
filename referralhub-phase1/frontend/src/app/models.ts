export type Role = 'CANDIDATE' | 'EMPLOYEE';

export interface Company {
  id: number; name: string; website: string; industry: string; description: string; createdAt?: string;
}

export interface User {
  id: number; name: string; email: string; role: Role;
}

export interface Job {
  id: number; jobCode: string; title: string; location: string; description: string;
  requiredSkills: string; preferredSkills: string; experience: string;
  company: Company; employee: User; createdAt: string; active: boolean;
}

export interface CandidateProfile {
  id?: number; candidate: User; skills: string; experience: string; projects: string;
  preferredRoles: string; preferredLocations: string; graduationYear?: number;
  phone?: string; currentLocation?: string;
}

export interface ReferralRequest {
  id: number; job: Job; candidate: User; employee: User; matchPercentage: number;
  matchedSkills: string; missingSkills: string; status: string; createdAt: string;
}

export interface ReferralAnalysis {
  matchPercentage: number; matchedSkills: string[]; missingSkills: string[];
  memories: string[]; explanation: string;
}
