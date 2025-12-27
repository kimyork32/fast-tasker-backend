package com.fasttasker.fast_tasker.application.dto.task;

import lombok.Builder;

@Builder
public record AnswerResponse(
        String id,
        String description,
        String questionId,
        String answeredId,
        String createdAt
) {}
