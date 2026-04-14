package ru.vk.education.job.repository;

import ru.vk.education.job.model.entity.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public void add(User user) {
        users.put(user.getName(), user);
    }

    public boolean contains(String name) {
        return users.containsKey(name);
    }

    public User findByName(String name) {
        return users.get(name);
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public List<User> getAllSorted() {
        return users.values().stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    public Set<String> getTopSkills(int limit) {
        return users.values().stream()
                .flatMap(user -> user.getSkills().stream())
                .collect(Collectors.groupingBy(skill -> skill, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }


}
