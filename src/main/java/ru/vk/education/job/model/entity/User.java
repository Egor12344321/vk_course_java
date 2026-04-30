package ru.vk.education.job.model.entity;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


public class User {
    private final String name;
    private final Set<String> skills;
    private final int experience;

    public User(String name, Set<String> skills, int experience) {
        this.name = name;
        this.skills = Set.copyOf(skills);
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        List<String> sortedSkills = new ArrayList<>(skills);
        sortedSkills.sort(String::compareTo);
        return name + " " + String.join(",", sortedSkills) + " " + experience;
    }

    public List<Job> findSuitableJobs(Collection<Job> availableJobs, int limit) {
        return availableJobs.stream()
                .filter(job -> job.isRelevantFor(this))
                .sorted((j1, j2) -> Double.compare(
                        j2.calculateMatchScore(this),
                        j1.calculateMatchScore(this)
                ))
                .limit(limit)
                .collect(Collectors.toList());
    }

    boolean hasSkill(String skill) {
        return skills.contains(skill);
    }

    public int getExperience() {
        return experience;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public int getMatchCount(Collection<Job> allJobs) {
        return (int) allJobs.stream()
                .filter(job -> job.calculateMatchScore(this) > 0)
                .count();
    }
    public Job getBestJobForUser(Collection<Job> jobs){
        if (jobs == null || jobs.isEmpty()) {
            return null;
        }

        return jobs.stream()
                .max((j1, j2) -> Double.compare(
                        j1.calculateMatchScore(this),
                        j2.calculateMatchScore(this)
                ))
                .orElse(null);
    }

}
