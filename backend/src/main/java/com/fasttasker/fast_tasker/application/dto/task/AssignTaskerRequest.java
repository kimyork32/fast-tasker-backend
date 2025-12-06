package com.fasttasker.fast_tasker.application.dto.task;

public record AssignTaskerRequest(
        String taskerId,
        String taskId
) {}
