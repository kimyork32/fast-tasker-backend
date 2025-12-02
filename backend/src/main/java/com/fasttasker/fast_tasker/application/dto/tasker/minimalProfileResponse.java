package com.fasttasker.fast_tasker.application.dto.tasker;

/**
 * DTO with minimal attributes of the profile, its will send to offer
 * and question
 */
public record minimalProfileResponse(
        String firstName,
        String lastName,
        String photo,
        int reputation,
        int clientReviews,
        int completedTasks
) {}