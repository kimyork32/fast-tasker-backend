package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

@Builder
public record ProfileResponse(
        String firstName,
        String lastName,
        String photo,
        String about,
        int reputation,
        int clientReviews,
        int completedTasks,
        LocationResponse location
) {
}
