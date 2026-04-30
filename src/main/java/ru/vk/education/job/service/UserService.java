package ru.vk.education.job.service;

import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;
import ru.vk.education.job.model.dto.UserCreateRequest;
import ru.vk.education.job.model.dto.UserResponse;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MapperService mapperService;

    public UserService(UserRepository userRepository, MapperService mapperService) {

        this.userRepository = userRepository;
        this.mapperService = mapperService;
    }

    public UserResponse addUser(UserCreateRequest userCreateRequest) {
        User user = mapperService.mapFromUserCreateRequestToEntity(userCreateRequest);
        if (userRepository.contains(user.getName())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        userRepository.add(user);

        return mapperService.mapFromUserEntityToResponse(user);
    }

    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден: " + name);
        }
        return mapperService.mapFromUserEntityToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.getAll().stream().map(mapperService::mapFromUserEntityToResponse).toList();
    }

    public List<UserResponse> getAllUsersSorted() {
        return userRepository.getAllSorted().stream().map(mapperService::mapFromUserEntityToResponse).toList();
    }

    public boolean userExists(String name) {
        return userRepository.contains(name);
    }


}