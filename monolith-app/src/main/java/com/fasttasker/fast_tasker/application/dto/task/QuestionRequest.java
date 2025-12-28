package com.fasttasker.fast_tasker.application.dto.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record QuestionRequest(

        @NotBlank(message = "Description cannot be empty")
        @Max(value = 500, message = "Description must be less than or equal to 500 characters")
        String description
) {}
