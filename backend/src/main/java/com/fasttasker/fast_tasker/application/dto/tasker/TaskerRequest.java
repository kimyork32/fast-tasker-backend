package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

@Builder(toBuilder = true)
public record TaskerRequest(
        String accountId,
        ProfileRequest profile
){}