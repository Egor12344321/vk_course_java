package ru.vk.education.job.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class SuggestServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Autowired
    private SuggestService suggestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM user_skills");
        jdbcTemplate.execute("DELETE FROM job_tags");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("DELETE FROM jobs");

        User alice = new User("Alice", Set.of("Java", "Spring", "SQL"), 3);
        User bob = new User("Bob", Set.of("Python", "Django"), 2);

        userRepository.save(alice);
        userRepository.save(bob);

        Job job1 = new Job("Senior Java Dev", "Google", Set.of("Java", "Spring", "Microservices"), 3);
        Job job2 = new Job("Python Backend", "Microsoft", Set.of("Python", "Django", "PostgreSQL"), 2);
        Job job3 = new Job("Android Developer", "JetBrains", Set.of("Java", "Kotlin", "Android"), 4);
        Job job4 = new Job("Frontend Developer", "Facebook", Set.of("JavaScript", "React", "CSS"), 1);

        jobRepository.save(job1);
        jobRepository.save(job2);
        jobRepository.save(job3);
        jobRepository.save(job4);
    }

    @Test
    void suggestTest_ShouldReturnCorrectJobs_WhenSeveralUsersAndJobsExist() {
        List<JobResponse> suggestionsForAlice = suggestService.suggestJobsForUser("Alice", 2);

        assertNotNull(suggestionsForAlice);
        assertEquals(2, suggestionsForAlice.size());
        assertEquals("Senior Java Dev", suggestionsForAlice.get(0).title());
        assertEquals("Android Developer", suggestionsForAlice.get(1).title());

        List<JobResponse> suggestionsForBob = suggestService.suggestJobsForUser("Bob", 2);

        assertNotNull(suggestionsForBob);
        assertEquals(1, suggestionsForBob.size());
        assertEquals("Python Backend", suggestionsForBob.get(0).title());
    }
}