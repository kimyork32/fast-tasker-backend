package com.fasttasker.fast_tasker.application.dto.task;

import com.fasttasker.fast_tasker.application.dto.tasker.MinimalProfileResponse;
import lombok.Builder;

@Builder
public record QuestionProfileResponse(
        QuestionResponse question,
        MinimalProfileResponse profile
) {}
