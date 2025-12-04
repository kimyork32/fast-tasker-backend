package com.fasttasker.fast_tasker.application.dto.task;

public record AnswerRequest(
        String questionId,
        String description
) {}
