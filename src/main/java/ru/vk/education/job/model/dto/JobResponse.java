package ru.vk.education.job.model.dto;

import java.util.Set;

public record JobResponse(
        String title,
        String company,
        Set<String> tags,
        int requiredExperience
) {
}
