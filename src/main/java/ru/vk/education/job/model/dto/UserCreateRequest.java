package ru.vk.education.job.model.dto;

import java.util.Set;

public record UserCreateRequest(
        String name,
        Set<String> skills,
        int experience
) {
}
