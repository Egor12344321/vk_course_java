package ru.vk.education.job.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.model.dto.JobCreateRequest;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.service.JobService;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<JobResponse>> getAllJobsSorted() {
        return ResponseEntity.ok(jobService.getAllJobsSorted());
    }

    @PostMapping
    public ResponseEntity<JobResponse> addJob(@RequestBody JobCreateRequest jobCreateRequest) {
        return ResponseEntity.ok(jobService.addJob(jobCreateRequest));
    }

}