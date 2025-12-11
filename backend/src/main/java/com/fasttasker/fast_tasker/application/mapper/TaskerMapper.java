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

    // To entity ////////////////////////////////////////
    public Profile toProfileEntity(ProfileRequest request) {
        if (request == null) return null;

        var loc = request.location();

        return Profile.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .photo(request.photo())
                .location(Location.builder()
                        .latitude(loc.latitude())
                        .longitude(loc.longitude())
                        .address(loc.address())
                        .zip(loc.zip())
                        .build())
                .about(request.about())
                .build();
    }

    public Tasker toTaskerEntity(TaskerRequest request) {
        if (request == null) return null;

        var profile = toProfileEntity(request.profile());

        return Tasker.builder()
                .accountId(UUID.fromString(request.accountId()))
                .profile(profile)
                .build();
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
                .id(tasker.getId().toString())
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
                .id(tasker.getId().toString())
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
                .offerId(request.offerId())
                .build();
    }

    public ChatProfileResponse toChatProfileResponse(Tasker tasker) {
        if (tasker == null) return null;

        return ChatProfileResponse.builder()
                .firstName(tasker.getProfile().getFirstName())
                .lastName(tasker.getProfile().getLastName())
                .photo(tasker.getProfile().getPhoto())
                .build();
    }
}
