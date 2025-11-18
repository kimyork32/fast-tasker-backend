package com.fasttasker.fast_tasker.application.dto.task;

import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;

public record TaskRequest(
        String title,
        String description,
        int budget,
        LocationRequest location,
        String taskDate
) {}
