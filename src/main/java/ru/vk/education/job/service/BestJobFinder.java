package ru.vk.education.job.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.List;

@Service
public class BestJobFinder implements Runnable {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public BestJobFinder(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    @Scheduled(fixedDelay = 5000)
    public void run() {
        List<User> users = userRepository.getAllSorted();
        List<Job> jobs = jobRepository.getAllSorted();

        if (users.isEmpty()) {
            return;
        }

        if (jobs.isEmpty()) {
            return;
        }

        for (User user : users) {
            Job bestJob = user.getBestJobForUser(jobs);
            if (bestJob != null) {
                System.out.printf("%s, лучшее предложение - %s\n", user.getName(), bestJob.toString());
            }
        }
    }
}