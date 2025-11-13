package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.TaskerResponse;
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
                request.profile().photo(),
                new Location(loc.latitude(), loc.longitude(), loc.address()),
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

        var locationResponse = TaskerResponse.LocationResponse.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .build();

        var profileResponse = TaskerResponse.ProfileResponse.builder()
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
}
