package com.referralhub.controller;

import com.referralhub.model.Job;
import com.referralhub.service.JobService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService service;
    public JobController(JobService service) { this.service = service; }

    @GetMapping("/latest")
    public List<Job> latest() { return service.latest(); }

    @GetMapping("/{id}")
    public Job find(@PathVariable Long id) { return service.find(id); }

    @PostMapping
    public Job create(@RequestBody Job job,
                      @RequestParam Long companyId,
                      @RequestParam Long employeeId) {
        return service.create(job, companyId, employeeId);
    }
}
