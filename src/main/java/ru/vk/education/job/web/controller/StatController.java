package ru.vk.education.job.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.dto.UserResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.service.StatService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/stat")
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/exp/{minExp}")
    public ResponseEntity<List<JobResponse>> getJobsByExp(@PathVariable int minExp) {
        return ResponseEntity.ok(statService.getJobsByMinExp(minExp));
    }

    @GetMapping("/match/{minMatch}")
    public ResponseEntity<List<UserResponse>> getUsersByMatchCount(@PathVariable int minMatch) {
        return ResponseEntity.ok(statService.getUsersByMinMatchCount(minMatch));
    }

    @GetMapping("/top-skills/{limit}")
    public ResponseEntity<Set<String>> getTopSkills(@PathVariable int limit) {
        return ResponseEntity.ok(statService.getTopSkills(limit));
    }
}