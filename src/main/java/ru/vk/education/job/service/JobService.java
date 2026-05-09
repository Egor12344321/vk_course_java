package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.model.dto.JobCreateRequest;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.repository.JobRepository;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final MapperService mapperService;

    public JobService(JobRepository jobRepository, MapperService mapperService) {
        this.jobRepository = jobRepository;
        this.mapperService = mapperService;
    }

    public JobResponse addJob(JobCreateRequest jobRequest) {
        Job job = mapperService.mapFromJobCreateRequestToEntity(jobRequest);
        if (jobRepository.existsByTitle(job.getTitle())) {
            throw new IllegalArgumentException("Вакансия с таким названием уже существует");
        }
        jobRepository.save(job);

        return mapperService.mapFromJobEntityToResponse(job);
    }


    public List<JobResponse> getAllJobs() {

        return jobRepository.findAll().stream()
                .map(mapperService::mapFromJobEntityToResponse)
                .toList();
    }

    public List<JobResponse> getAllJobsSorted() {
        return jobRepository.getAllSorted().stream()
                .map(mapperService::mapFromJobEntityToResponse)
                .toList();
    }

    public boolean jobExists(String title) {
        return jobRepository.existsByTitle(title);
    }
}