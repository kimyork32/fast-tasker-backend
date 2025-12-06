package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.task.AssignTaskerRequest;
import com.fasttasker.fast_tasker.application.dto.task.AssignTaskerResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.*;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskerMapper {

    public Tasker toTaskerEntity(TaskerRequest request) {
        if (request == null) return null;

        var location = new Location(
                request.profile().location().latitude(),
                request.profile().location().longitude(),
                request.profile().location().address(),
                request.profile().location().zip()
        );

        var profile = Profile.builder()
                .firstName(request.profile().firstName())
                .lastName(request.profile().lastName())
                .photo(request.profile().photo())
                .about(request.profile().about())
                .reputation(request.profile().reputation())
                .clientReviews(request.profile().clientReviews())
                .completedTasks(request.profile().completedTasks())
                .location(location)
                .build();

        return Tasker.builder()
                .id(UUID.randomUUID())
                .accountId(request.accountId())
                .profile(profile)
                .build();
    }

    public Profile toProfileEntity(ProfileRequest request) {
        if (request == null) return null;

        var loc = request.location();

        return new Profile(
                request.firstName(),
                request.lastName(),
                request.photo(),
                new Location(loc.latitude(), loc.longitude(), loc.address(), loc.zip()),
                request.about(),
                request.reputation(),
                request.clientReviews(),
                request.completedTasks()
        );
    }

    // To response ////////////////////////////////////////
    public TaskerResponse toResponse(Tasker tasker) {
        if (tasker == null) return null;

        var profile = tasker.getProfile();
        var location = profile.getLocation();

        var locationResponse = LocationResponse.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .zip(location.getZip())
                .build();

        var profileResponse = ProfileResponse.builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .photo(profile.getPhoto())
                .about(profile.getAbout())
                .reputation(profile.getReputation())
                .clientReviews(profile.getClientReviews())
                .completedTasks(profile.getCompletedTasks())
                .location(locationResponse)
                .build();

        return TaskerResponse.builder()
                .id(tasker.getId())
                .accountId(tasker.getAccountId())
                .profile(profileResponse)
                .build();
    }

    // TO RESPONSE ////////////////////////////////////////////////////////
    public MinimalProfileResponse toMinimalProfileResponse(Tasker tasker) {
        if (tasker == null) return null;

        var profile = tasker.getProfile();

        return MinimalProfileResponse.builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .photo(profile.getPhoto())
                .reputation(profile.getReputation())
                .clientReviews(profile.getClientReviews())
                .completedTasks(profile.getCompletedTasks())
                .build();
    }

    public AssignTaskerResponse toAssignTaskerResponse(AssignTaskerRequest request) {
        if (request == null) return null;

        return AssignTaskerResponse.builder()
                .taskerId(request.taskerId())
                .taskId(request.taskId())
                .build();
    }
}
