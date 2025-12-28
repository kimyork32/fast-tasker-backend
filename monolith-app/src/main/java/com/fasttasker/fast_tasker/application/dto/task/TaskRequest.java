package com.fasttasker.fast_tasker.application.dto.task;

import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record TaskRequest(
        @NotBlank(message = "Title cannot be empty")
        String title,

        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotBlank(message = "Price cannot be empty")
        @Min(value = 5, message = "Price must be greater than or equal to 5")
        @Max(value = 999, message = "Price must be less than or equal to 999")
        int budget,

        @NotBlank(message = "Location cannot be empty")
        LocationRequest location,

        @NotBlank(message = "Task date cannot be empty")
        String taskDate
) {}
