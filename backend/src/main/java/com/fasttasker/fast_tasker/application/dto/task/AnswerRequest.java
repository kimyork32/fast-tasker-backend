package com.fasttasker.fast_tasker.application.dto.task;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequest(
        @NotBlank(message = "Question ID cannot be blank")
        String questionId,

        @NotBlank(message = "Description cannot be blank")
        String description
) {}
