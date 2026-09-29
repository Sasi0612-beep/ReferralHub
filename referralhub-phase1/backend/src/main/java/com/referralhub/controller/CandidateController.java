package com.referralhub.controller;

import com.referralhub.model.CandidateProfile;
import com.referralhub.service.CandidateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService service;
    public CandidateController(CandidateService service) { this.service = service; }

    @GetMapping("/{id}/profile")
    public CandidateProfile get(@PathVariable Long id) { return service.get(id); }

    @PostMapping("/{id}/profile")
    public CandidateProfile save(@PathVariable Long id, @RequestBody CandidateProfile profile) {
        return service.save(profile, id);
    }
}
