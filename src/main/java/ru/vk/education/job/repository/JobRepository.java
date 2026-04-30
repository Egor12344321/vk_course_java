package ru.vk.education.job.repository;

import org.springframework.stereotype.Repository;
import ru.vk.education.job.model.entity.Job;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public class JobRepository {
    private final Map<String, Job> jobs = new HashMap<>();

    public void add(Job job) {
        jobs.put(job.getTitle(), job);
    }

    public boolean contains(String title) {
        return jobs.containsKey(title);
    }

    public Collection<Job> getAll() {
        return jobs.values();
    }

    public List<Job> getAllSorted() {
        return jobs.values().stream()
                .sorted(Comparator.comparing(Job::getTitle))
                .collect(Collectors.toList());
    }

}
