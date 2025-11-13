package com.fasttasker.fast_tasker.application.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TaskerResponse(
    UUID id,
    UUID accountId,
    ProfileResponse profile
) {
    @Builder
    public record ProfileResponse(
            String photo,
            String about,
            int reputation,
            int clientReviews,
            int completedTasks,
            LocationResponse location
    ) {}

    @Builder
    public record LocationResponse(
            double latitude,
            double longitude,
            String address
    ) {}
}