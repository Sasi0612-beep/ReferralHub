package com.referralhub.service;

import com.referralhub.model.*;
import com.referralhub.repository.*;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    private final CandidateProfileRepository profiles;
    private final UserRepository users;
    private final HindsightService hindsight;

    public CandidateService(CandidateProfileRepository profiles, UserRepository users, HindsightService hindsight) {
        this.profiles = profiles;
        this.users = users;
        this.hindsight = hindsight;
    }

    public CandidateProfile get(Long candidateId) {
        return profiles.findByCandidateId(candidateId).orElseGet(() -> {
            User user = users.findById(candidateId).orElseThrow();
            return CandidateProfile.builder().candidate(user).build();
        });
    }

    public CandidateProfile save(CandidateProfile profile, Long candidateId) {
        User candidate = users.findById(candidateId).orElseThrow();
        CandidateProfile saved = profiles.findByCandidateId(candidateId).orElseGet(CandidateProfile::new);
        saved.setCandidate(candidate);
        saved.setSkills(profile.getSkills());
        saved.setExperience(profile.getExperience());
        saved.setProjects(profile.getProjects());
        saved.setPreferredRoles(profile.getPreferredRoles());
        saved.setPreferredLocations(profile.getPreferredLocations());
        saved.setGraduationYear(profile.getGraduationYear());
        saved.setPhone(profile.getPhone());
        saved.setCurrentLocation(profile.getCurrentLocation());
        CandidateProfile result = profiles.save(saved);

        hindsight.retain(
                "candidate-" + candidateId,
                String.format("Candidate %s has skills: %s. Experience: %s. Projects: %s. Preferred roles: %s. Graduation year: %s. Current location: %s.",
                        candidate.getName(), result.getSkills(), result.getExperience(), result.getProjects(),
                        result.getPreferredRoles(), result.getGraduationYear(), result.getCurrentLocation()),
                "Candidate profile update"
        );
        return result;
    }
}
