package ru.vk.education.job.model.dto;

import java.util.Set;

public record JobCreateRequest(
        String title,
        String company,
        Set<String>tags,
        int requiredExperience
) {
}
