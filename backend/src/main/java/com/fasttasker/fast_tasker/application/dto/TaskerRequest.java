package com.fasttasker.fast_tasker.application.dto;

import lombok.Builder;

import java.util.UUID;

public record TaskerRequest(
        UUID accountId,
        ProfileRequest profile
) {
    @Builder
    public record ProfileRequest(
            String photo,
            String about,
            int reputation,
            int clientReviews,
            int completedTasks,
            LocationRequest location
    ) {}

    @Builder
    public record LocationRequest(
            double latitude,
            double longitude,
            String address
    ) {}
}