package com.referralhub.service;

import com.referralhub.model.*;
import com.referralhub.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobs;
    private final CompanyRepository companies;
    private final UserRepository users;
    private final HindsightService hindsight;

    public JobService(JobRepository jobs, CompanyRepository companies, UserRepository users, HindsightService hindsight) {
        this.jobs = jobs;
        this.companies = companies;
        this.users = users;
        this.hindsight = hindsight;
    }

    public List<Job> latest() { return jobs.findTop20ByActiveTrueOrderByCreatedAtDesc(); }

    public Job find(Long id) { return jobs.findById(id).orElseThrow(); }

    public Job create(Job job, Long companyId, Long employeeId) {
        Company company = companies.findById(companyId).orElseThrow();
        User employee = users.findById(employeeId).orElseThrow();
        if (employee.getRole() != User.UserRole.EMPLOYEE) throw new IllegalArgumentException("Only employees can post jobs");
        job.setCompany(company);
        job.setEmployee(employee);
        job.setCreatedAt(LocalDateTime.now());
        job.setActive(true);
        Job saved = jobs.save(job);
        hindsight.retain(
                "job-" + saved.getJobCode(),
                String.format("Job %s at %s. Role: %s. Required skills: %s. Preferred skills: %s. Location: %s. Posted by %s.",
                        saved.getJobCode(), company.getName(), saved.getTitle(), saved.getRequiredSkills(),
                        saved.getPreferredSkills(), saved.getLocation(), employee.getName()),
                "Referral opportunity posted"
        );
        return saved;
    }
}
