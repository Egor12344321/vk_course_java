package ru.vk.education.job.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vk.education.job.model.entity.User;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String userSql = "INSERT INTO users (name, experience) VALUES (?, ?)";
        jdbcTemplate.update(userSql, user.getName(), user.getExperience());

        for (String skill : user.getSkills()) {
            String skillSql = "INSERT INTO user_skills (user_name, skill) VALUES (?, ?)";
            jdbcTemplate.update(skillSql, user.getName(), skill);
        }
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM users WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public User findByName(String name) {
        String sql = "SELECT name, experience FROM users WHERE name = ?";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            String userName = rs.getString("name");
            int experience = rs.getInt("experience");
            Set<String> skills = getSkillsByUserName(userName);
            return new User(userName, skills, experience);
        }, name);

        return users.isEmpty() ? null : users.get(0);
    }

    private Set<String> getSkillsByUserName(String name) {
        String sql = "SELECT skill FROM user_skills WHERE user_name = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, name));
    }

    public Collection<User> findAll() {
        String sql = "SELECT u.name, u.experience, s.skill FROM users u " +
                "LEFT JOIN user_skills s ON u.name = s.user_name " +
                "ORDER BY u.name";

        Map<String, User> userMap = new LinkedHashMap<>();

        jdbcTemplate.query(sql, (rs) -> {
            String name = rs.getString("name");
            int experience = rs.getInt("experience");
            String skill = rs.getString("skill");

            User user = userMap.get(name);
            if (user == null) {
                Set<String> skills = new HashSet<>();
                if (skill != null) {
                    skills.add(skill);
                }
                user = new User(name, skills, experience);
                userMap.put(name, user);
            } else if (skill != null) {
                user.getSkills().add(skill);
            }
        });

        return userMap.values();
    }

    public List<User> getAllSorted() {
        return findAll().stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    public Set<String> getTopSkills(int limit) {
        String sql = "SELECT skill, COUNT(*) as cnt FROM user_skills " +
                "GROUP BY skill " +
                "ORDER BY cnt DESC, skill ASC " +
                "LIMIT ?";

        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, limit));
    }
}
