package com.referralhub.repository;

import com.referralhub.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findTop20ByActiveTrueOrderByCreatedAtDesc();
    Optional<Job> findByJobCode(String jobCode);
}
