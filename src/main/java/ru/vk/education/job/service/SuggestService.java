package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.dto.UserResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class SuggestService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final UserService userService;
    private final MapperService mapperService;

    public SuggestService(UserRepository userRepository,
                          JobRepository jobRepository,
                          UserService userService,
                          MapperService mapperService) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.userService = userService;
        this.mapperService = mapperService;
    }

    public List<JobResponse> suggestJobsForUser(String username, int limit) {
        User user = mapperService.mapFromResponseToEntity(userService.getUserByName(username));
        Collection<Job> allJobs = jobRepository.getAll();

        return user.findSuitableJobs(allJobs, limit).stream()
                .map(mapperService::mapFromJobEntityToResponse)
                .toList();
    }

    public JobResponse getBestJobForUser(String username) {
        User user = mapperService.mapFromResponseToEntity(userService.getUserByName(username));
        Collection<Job> allJobs = jobRepository.getAll();

        Job bestJob = user.getBestJobForUser(allJobs);

        if (bestJob == null) {
            return null;
        }

        return mapperService.mapFromJobEntityToResponse(bestJob);
    }
}