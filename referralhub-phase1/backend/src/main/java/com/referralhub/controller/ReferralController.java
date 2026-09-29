package com.referralhub.controller;

import com.referralhub.dto.ReferralAnalysis;
import com.referralhub.model.ReferralRequest;
import com.referralhub.service.ReferralService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/referrals")
public class ReferralController {
    private final ReferralService service;
    public ReferralController(ReferralService service) { this.service = service; }

    @GetMapping("/analysis")
    public ReferralAnalysis analysis(@RequestParam Long jobId, @RequestParam Long candidateId) {
        return service.analyze(jobId, candidateId);
    }

    @PostMapping
    public ReferralRequest create(@RequestParam Long jobId, @RequestParam Long candidateId) {
        return service.create(jobId, candidateId);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ReferralRequest> employeeRequests(@PathVariable Long employeeId) {
        return service.employeeRequests(employeeId);
    }

    @PatchMapping("/{id}/decision")
    public ReferralRequest decide(@PathVariable Long id, @RequestParam ReferralRequest.ReferralStatus status) {
        return service.decide(id, status);
    }
}
