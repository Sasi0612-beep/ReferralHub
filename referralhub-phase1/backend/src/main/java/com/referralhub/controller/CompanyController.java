package com.referralhub.controller;

import com.referralhub.model.Company;
import com.referralhub.service.CompanyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService service;
    public CompanyController(CompanyService service) { this.service = service; }

    @GetMapping
    public List<Company> all() { return service.all(); }

    @PostMapping
    public Company create(@RequestBody Company company) { return service.create(company); }
}
