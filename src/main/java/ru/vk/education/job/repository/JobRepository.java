package ru.vk.education.job.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vk.education.job.model.entity.Job;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public class JobRepository {

    private final JdbcTemplate jdbcTemplate;

    public JobRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Job job) {
        String sql = "INSERT INTO jobs (title, company, required_experience) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, job.getTitle(), job.getCompany(), job.getRequiredExperience());

        for (String tag : job.getTags()) {
            String tagSql = "INSERT INTO job_tags (job_title, tag) VALUES (?, ?)";
            jdbcTemplate.update(tagSql, job.getTitle(), tag);
        }
    }

    public boolean existsByTitle(String title) {
        String sql = "SELECT COUNT(*) FROM jobs WHERE title = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, title);
        return count != null && count > 0;
    }

    public Collection<Job> findAll() {
        String sql = """
            SELECT j.title, j.company, j.required_experience, t.tag
            FROM jobs j
            LEFT JOIN job_tags t ON j.title = t.job_title
            ORDER BY j.title
        """;

        Map<String, Job> jobMap = new LinkedHashMap<>();

        jdbcTemplate.query(sql, (rs) -> {
            String title = rs.getString("title");
            String company = rs.getString("company");
            int requiredExp = rs.getInt("required_experience");
            String tag = rs.getString("tag");

            Job job = jobMap.get(title);
            if (job == null) {
                Set<String> tags = new HashSet<>();
                if (tag != null) {
                    tags.add(tag);
                }
                job = new Job(title, company, tags, requiredExp);
                jobMap.put(title, job);
            } else if (tag != null) {
                job.getTags().add(tag);
            }
        });

        return jobMap.values();
    }

    public List<Job> getAllSorted() {
        return findAll().stream()
                .sorted(Comparator.comparing(Job::getTitle))
                .collect(Collectors.toList());
    }
}