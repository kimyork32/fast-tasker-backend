package com.fasttasker.fast_tasker.application.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record AnswerRequest(
        @NotBlank(message = "Question ID cannot be blank")
        String questionId,

        @NotBlank(message = "Description cannot be blank")
        String description
) {}
