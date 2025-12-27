package com.fasttasker.fast_tasker.application.dto.task;

import com.fasttasker.fast_tasker.application.dto.tasker.ChatProfileResponse;
import lombok.Builder;

@Builder
public record AnswerProfileResponse(
        AnswerResponse answer,
        ChatProfileResponse profile
) {}