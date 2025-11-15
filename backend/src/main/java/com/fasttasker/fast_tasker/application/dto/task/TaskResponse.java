package com.fasttasker.fast_tasker.application.dto.task;

import com.fasttasker.fast_tasker.application.dto.tasker.LocationResponse;

public record TaskResponse(
        String title,
        String description,
        int budget,
        LocationResponse location,
        String taskDate,
        String status,
        String posterId
) {}
