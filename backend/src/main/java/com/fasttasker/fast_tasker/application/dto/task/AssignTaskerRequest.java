package com.fasttasker.fast_tasker.application.dto.task;

import jakarta.validation.constraints.NotBlank;

public record AssignTaskerRequest(
        @NotBlank(message = "taskerId cannot be empty") String taskerId,
        @NotBlank(message = "taskId cannot be empty") String taskId,
        @NotBlank(message = "taskId cannot be empty") String offerId
) {}
