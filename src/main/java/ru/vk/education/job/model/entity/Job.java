package ru.vk.education.job.model.entity;

import org.springframework.stereotype.Component;

import java.util.Set;



public class Job {
    private final String title;
    private final String company;
    private final Set<String> tags;
    private final int requiredExperience;

    public Job(String title, String company, Set<String> tags, int requiredExperience) {
        this.title = title;
        this.company = company;
        this.tags = Set.copyOf(tags);
        this.requiredExperience = requiredExperience;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany(){return company;}

    public Set<String> getTags(){return tags;}

    public int getRequiredExperience(){return requiredExperience;}

    public String toString() {
        return title + " at " + company;
    }

    boolean isRelevantFor(User user) {
        return calculateMatchScore(user) > 0;
    }

    double calculateMatchScore(User user) {

        double score = tags.stream()
                .filter(user::hasSkill)
                .count();

        if (user.getExperience() < requiredExperience) {
            score = score / 2.0;
        }

        return score;
    }

    public Integer getRequiredExp(){
        return this.requiredExperience;
    }
}
