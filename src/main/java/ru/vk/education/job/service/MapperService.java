package ru.vk.education.job.service;


import org.springframework.stereotype.Service;
import ru.vk.education.job.model.dto.JobCreateRequest;
import ru.vk.education.job.model.dto.JobResponse;
import ru.vk.education.job.model.dto.UserCreateRequest;
import ru.vk.education.job.model.dto.UserResponse;
import ru.vk.education.job.model.entity.Job;
import ru.vk.education.job.model.entity.User;

@Service
public class MapperService {

    public User mapFromUserCreateRequestToEntity(UserCreateRequest request){
        return new User(request.name(), request.skills(), request.experience());
    }

    public Job mapFromJobCreateRequestToEntity(JobCreateRequest request){
        return new Job(request.title(), request.company(), request.tags(), request.requiredExperience());
    }

    public JobResponse mapFromJobEntityToResponse(Job job){
        return new JobResponse(job.getTitle(), job.getCompany(), job.getTags(), job.getRequiredExperience());
    }

    public UserResponse mapFromUserEntityToResponse(User user){
        return new UserResponse(user.getName(), user.getSkills(), user.getExperience());
    }

    public User mapFromResponseToEntity(UserResponse response){
        return new User(response.name(), response.skills(), response.experience());
    }
}
