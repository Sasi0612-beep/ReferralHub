package com.referralhub.service;

import com.referralhub.dto.*;
import com.referralhub.model.*;
import com.referralhub.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReferralService {
    private final ReferralRequestRepository referrals;
    private final JobRepository jobs;
    private final UserRepository users;
    private final CandidateProfileRepository profiles;
    private final MatchingService matching;
    private final HindsightService hindsight;

    public ReferralService(ReferralRequestRepository referrals, JobRepository jobs, UserRepository users,
                           CandidateProfileRepository profiles, MatchingService matching,
                           HindsightService hindsight) {
        this.referrals = referrals;
        this.jobs = jobs;
        this.users = users;
        this.profiles = profiles;
        this.matching = matching;
        this.hindsight = hindsight;
    }

    public ReferralAnalysis analyze(Long jobId, Long candidateId) {
        Job job = jobs.findById(jobId).orElseThrow();
        CandidateProfile profile = profiles.findByCandidateId(candidateId).orElseThrow();
        MatchResult match = matching.calculate(profile.getSkills(), job.getRequiredSkills(), job.getPreferredSkills());

        String query = String.format("Candidate %s for job %s at %s. Skills: %s. Match: %d%%. Previous referral interactions, employee decisions and job context.",
                profile.getCandidate().getName(), job.getJobCode(), job.getCompany().getName(), profile.getSkills(), match.percentage());

        List<String> memories = new ArrayList<>();
        memories.addAll(hindsight.recall("candidate-" + candidateId, query));
        memories.addAll(hindsight.recall("employee-" + job.getEmployee().getId(), query));
        memories.addAll(hindsight.recall("job-" + job.getJobCode(), query));
        memories = memories.stream().distinct().limit(6).toList();

        String explanation = String.format(
                "%d%% match based on the candidate profile. %d skills matched and %d required skills are missing. %s",
                match.percentage(), match.matchedSkills().size(), match.missingSkills().size(),
                memories.isEmpty() ? "No historical Hindsight context was recalled yet." : "Historical context was recalled from Hindsight across the candidate, employee and job memory scopes."
        );

        return new ReferralAnalysis(match.percentage(), match.matchedSkills(), match.missingSkills(), memories, explanation);
    }

    public ReferralRequest create(Long jobId, Long candidateId) {
        Job job = jobs.findById(jobId).orElseThrow();
        User candidate = users.findById(candidateId).orElseThrow();
        CandidateProfile profile = profiles.findByCandidateId(candidateId).orElseThrow();

        Optional<ReferralRequest> existing = referrals.findByJobIdAndCandidateId(jobId, candidateId);
        if (existing.isPresent()) return existing.get();

        MatchResult match = matching.calculate(profile.getSkills(), job.getRequiredSkills(), job.getPreferredSkills());
        ReferralRequest request = ReferralRequest.builder()
                .job(job).candidate(candidate).employee(job.getEmployee())
                .matchPercentage(match.percentage())
                .matchedSkills(String.join(", ", match.matchedSkills()))
                .missingSkills(String.join(", ", match.missingSkills()))
                .status(ReferralRequest.ReferralStatus.PENDING)
                .createdAt(LocalDateTime.now()).build();
        ReferralRequest saved = referrals.save(request);

        hindsight.retain("candidate-" + candidateId,
                String.format("Candidate %s requested a referral for job %s at %s. Match was %d%%. Matched skills: %s. Missing skills: %s.",
                        candidate.getName(), job.getJobCode(), job.getCompany().getName(), match.percentage(),
                        String.join(", ", match.matchedSkills()), String.join(", ", match.missingSkills())),
                "Referral request");
        hindsight.retain("employee-" + job.getEmployee().getId(),
                String.format("Employee %s received a referral request from candidate %s for job %s. Candidate match was %d%%.",
                        job.getEmployee().getName(), candidate.getName(), job.getJobCode(), match.percentage()),
                "Employee referral activity");
        return saved;
    }

    public List<ReferralRequest> employeeRequests(Long employeeId) {
        return referrals.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    public ReferralRequest decide(Long referralId, ReferralRequest.ReferralStatus status) {
        if (status != ReferralRequest.ReferralStatus.REFERRED && status != ReferralRequest.ReferralStatus.DECLINED) {
            throw new IllegalArgumentException("Decision must be REFERRED or DECLINED");
        }
        ReferralRequest request = referrals.findById(referralId).orElseThrow();
        request.setStatus(status);
        ReferralRequest saved = referrals.save(request);
        hindsight.retain("employee-" + request.getEmployee().getId(),
                String.format("Employee %s decided %s on candidate %s for job %s. Match was %d%%.",
                        request.getEmployee().getName(), status, request.getCandidate().getName(),
                        request.getJob().getJobCode(), request.getMatchPercentage()),
                "Referral decision");
        hindsight.retain("candidate-" + request.getCandidate().getId(),
                String.format("Candidate %s received a %s decision from employee %s for job %s.",
                        request.getCandidate().getName(), status, request.getEmployee().getName(), request.getJob().getJobCode()),
                "Referral outcome");
        return saved;
    }
}
