package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.dto.UserResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class StatService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final MapperService mapperService;

    public StatService(UserRepository userRepository, JobRepository jobRepository, MapperService mapperService) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.mapperService = mapperService;
    }

    public List<JobResponse> getJobsByMinExp(int minExp) {
        return jobRepository.getAllSorted().stream()
                .filter(job -> job.getRequiredExp() >= minExp)
                .map(mapperService::mapFromJobEntityToResponse)
                .toList();
    }

    public List<UserResponse> getUsersByMinMatchCount(int minMatch) {
        return userRepository.getAllSorted().stream()
                .filter(user -> user.getMatchCount(jobRepository.getAll()) >= minMatch)
                .map(mapperService::mapFromUserEntityToResponse)
                .toList();
    }

    public Set<String> getTopSkills(int limit) {
        return userRepository.getTopSkills(limit);
    }
}
