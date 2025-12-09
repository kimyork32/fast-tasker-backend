package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

@Builder(toBuilder = true)
public record ProfileRequest(
        String firstName,
        String lastName,
        String photo,
        String about,
        int reputation,
        int clientReviews,
        int completedTasks,
        LocationRequest location
) {
}
