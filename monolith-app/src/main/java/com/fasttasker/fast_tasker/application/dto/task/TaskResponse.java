package com.fasttasker.fast_tasker.application.dto.task;

import com.fasttasker.fast_tasker.application.dto.tasker.LocationResponse;
import lombok.Builder;

@Builder
public record TaskResponse(
        String id,
        String title,
        String description,
        int budget,
        LocationResponse location,
        String taskDate,
        String status,
        String posterId
) {}
