package ru.vk.education.job.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.service.SuggestService;

import java.util.List;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    private final SuggestService suggestService;

    public SuggestController(SuggestService suggestService) {
        this.suggestService = suggestService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<JobResponse>> suggestForUser(@PathVariable String username) {
        return ResponseEntity.ok(suggestService.suggestJobsForUser(username, 2));
    }

    @GetMapping("/best/{username}")
    public ResponseEntity<JobResponse> getBestJobForUser(@PathVariable String username) {
        JobResponse response = suggestService.getBestJobForUser(username);
        return ResponseEntity.ok(response);
    }
}