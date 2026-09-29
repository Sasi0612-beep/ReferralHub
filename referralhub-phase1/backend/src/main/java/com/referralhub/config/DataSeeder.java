package com.referralhub.config;

import com.referralhub.model.*;
import com.referralhub.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(UserRepository users, CompanyRepository companies, JobRepository jobs,
                           CandidateProfileRepository profiles) {
        return args -> {
            if (users.count() > 0) return;

            User employee = users.save(User.builder().name("Ananya Sharma").email("ananya@referralhub.demo")
                    .password("employee123").role(User.UserRole.EMPLOYEE).build());
            User candidate = users.save(User.builder().name("Rahul Kumar").email("rahul@referralhub.demo")
                    .password("candidate123").role(User.UserRole.CANDIDATE).build());

            Company microsoft = companies.save(Company.builder().name("Microsoft").website("https://microsoft.com")
                    .industry("Technology").description("Software and cloud products").createdAt(LocalDateTime.now()).build());
            Company thales = companies.save(Company.builder().name("Thales").website("https://thalesgroup.com")
                    .industry("Technology").description("Digital identity, aerospace and security technology").createdAt(LocalDateTime.now()).build());

            jobs.save(Job.builder().jobCode("MS-SDE-1024").title("Software Engineer - Backend")
                    .location("Hyderabad").description("Build reliable backend services and APIs for large-scale products.")
                    .requiredSkills("Java, SQL, REST APIs").preferredSkills("Spring Boot, Docker, Azure")
                    .experience("0-2 years").company(microsoft).employee(employee).createdAt(LocalDateTime.now().minusHours(2)).active(true).build());
            jobs.save(Job.builder().jobCode("TH-SWE-2048").title("Software Developer")
                    .location("Bangalore").description("Develop maintainable enterprise software and modern web services.")
                    .requiredSkills("Java, Angular, REST APIs").preferredSkills("Spring Boot, Git")
                    .experience("0-2 years").company(thales).employee(employee).createdAt(LocalDateTime.now().minusHours(1)).active(true).build());

            profiles.save(CandidateProfile.builder().candidate(candidate)
                    .skills("Java, Spring Boot, SQL, REST APIs, Angular, Git")
                    .experience("Software Developer Intern")
                    .projects("AI-Powered Quiz Application; Malware Detection Engine")
                    .preferredRoles("Backend Developer, Software Engineer")
                    .preferredLocations("Hyderabad, Bangalore")
                    .graduationYear(2026).currentLocation("Andhra Pradesh").build());
        };
    }
}
