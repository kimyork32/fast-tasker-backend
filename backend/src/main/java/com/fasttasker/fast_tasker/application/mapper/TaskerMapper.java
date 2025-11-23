package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.tasker.*;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskerMapper {

    public Tasker toEntity(TaskerRequest request) {
        if (request == null) return null;

        var loc = request.profile().location();

        var profile = new Profile(
                request.profile().firstName(),
                request.profile().lastName(),
                request.profile().photo(),
                new Location(loc.latitude(), loc.longitude(), loc.address(), loc.zip()),
                request.profile().about(),
                request.profile().reputation(),
                request.profile().clientReviews(),
                request.profile().completedTasks()
        );

        return new Tasker(
                UUID.randomUUID(),
                request.accountId(),
                profile
        );
    }

    public TaskerResponse toResponse(Tasker tasker) {
        if (tasker == null) return null;

        var profile = tasker.getProfile();
        var location = profile.getLocation();

        var locationResponse = LocationResponse.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
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

    public Profile toProfileEntity(ProfileRequest request) {
        if (request == null) return null;

        var loc = request.location();

        return new Profile(
                request.firstName(),
                request.lastName(),
                request.photo(),
                new Location(loc.latitude(), loc.longitude(), loc.address(), loc.zip()),
                request
                        .about(),
                request.reputation(),
                request.clientReviews(),
                request.completedTasks()
                );
    }
}
