package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

/**
 * DTO with minimal attributes of the profile, its will send to offer
 * and question
 */
@Builder
public record MinimalProfileResponse(
        String id,
        String firstName,
        String lastName,
        String photo,
        int reputation,
        int clientReviews,
        int completedTasks
) {}