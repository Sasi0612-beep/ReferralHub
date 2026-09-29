package com.referralhub.repository;

import com.referralhub.model.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import java.util.List;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByCandidateId(Long candidateId);
}
