package com.referralhub.repository;

import com.referralhub.model.ReferralRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReferralRequestRepository extends JpaRepository<ReferralRequest, Long> {
    List<ReferralRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
    Optional<ReferralRequest> findByJobIdAndCandidateId(Long jobId, Long candidateId);
}
