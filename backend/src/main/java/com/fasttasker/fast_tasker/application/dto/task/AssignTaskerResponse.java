package com.fasttasker.fast_tasker.application.dto.task;

import lombok.Builder;

@Builder
public record AssignTaskerResponse(
        String taskerId,
        String taskId,
        String offerId
) {}
