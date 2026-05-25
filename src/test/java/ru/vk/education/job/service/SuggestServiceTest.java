package ru.vk.education.job.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private SuggestService suggestService;

    private User testUser;
    private Job job1;
    private Job job2;
    private Job job3;
    private JobResponse response1;
    private JobResponse response2;

    @BeforeEach
    void setUp() {
        Set<String> skills = new HashSet<>(Set.of("Java", "Spring", "SQL"));
        testUser = new User("Alice", skills, 3);

        Set<String> tags1 = new HashSet<>(Set.of("Java", "Spring", "Microservices"));
        job1 = new Job("Senior Java Dev", "Google", tags1, 3);

        Set<String> tags2 = new HashSet<>(Set.of("Python", "Django", "PostgreSQL"));
        job2 = new Job("Python Backend", "Microsoft", tags2, 2);

        Set<String> tags3 = new HashSet<>(Set.of("Java", "Kotlin", "Android"));
        job3 = new Job("Android Developer", "JetBrains", tags3, 4);

        response1 = new JobResponse("Senior Java Dev", "Google", Set.of("Java", "Spring", "Microservices"), 3);
        response2 = new JobResponse("Android Developer", "JetBrains", Set.of("Java", "Kotlin", "Android"), 4);
    }

    @Test
    void suggestTest_ShouldReturnSuitableJobs_WhenUserExistsAndSeveralJobsExist() {
        when(userRepository.findByName("Alice")).thenReturn(testUser);
        when(jobRepository.findAll()).thenReturn(List.of(job1, job2, job3));
        when(mapperService.mapFromJobEntityToResponse(job1)).thenReturn(response1);
        when(mapperService.mapFromJobEntityToResponse(job3)).thenReturn(response2);

        List<JobResponse> result = suggestService.suggestJobsForUser("Alice", 2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Senior Java Dev", result.get(0).title());
        assertEquals("Android Developer", result.get(1).title());

        verify(userRepository, times(1)).findByName("Alice");
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void emptyVacanciesTest_ShouldReturnEmptyList_WhenNoVacanciesExist() {
        when(userRepository.findByName("Alice")).thenReturn(testUser);
        when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        List<JobResponse> result = suggestService.suggestJobsForUser("Alice", 2);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByName("Alice");
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void singleVacancyTest_ShouldReturnSingleVacancy_WhenOnlyOneMatchingExists() {
        when(userRepository.findByName("Alice")).thenReturn(testUser);
        when(jobRepository.findAll()).thenReturn(List.of(job1));
        when(mapperService.mapFromJobEntityToResponse(job1)).thenReturn(response1);

        List<JobResponse> result = suggestService.suggestJobsForUser("Alice", 2);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Senior Java Dev", result.get(0).title());
    }

    @Test
    void userNotFoundTest_ShouldReturnEmptyList_WhenUserDoesNotExist() {
        when(userRepository.findByName("Unknown")).thenReturn(null);

        List<JobResponse> result = suggestService.suggestJobsForUser("Unknown", 2);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByName("Unknown");
        verify(jobRepository, never()).findAll();
    }
}