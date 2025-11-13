package com.fasttasker.fast_tasker.application.dto.tasker;

public record ProfileRequest(
        String photo,
        String about,
        int reputation,
        int clientReviews,
        int completedTasks,
        LocationRequest location
) {
}
