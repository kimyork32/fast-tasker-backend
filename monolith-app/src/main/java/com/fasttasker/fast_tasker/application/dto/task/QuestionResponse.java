package com.fasttasker.fast_tasker.application.dto.task;

import lombok.Builder;

@Builder
public record QuestionResponse(
        String id,
        String description,
        String status,
        String createAt
) {}
