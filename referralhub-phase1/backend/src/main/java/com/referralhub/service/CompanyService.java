package com.referralhub.service;

import com.referralhub.model.Company;
import com.referralhub.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository repository;
    public CompanyService(CompanyRepository repository) { this.repository = repository; }

    public List<Company> all() { return repository.findAll(); }

    public Company create(Company company) {
        company.setCreatedAt(LocalDateTime.now());
        return repository.save(company);
    }

    public Company find(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
