package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record TaskerRequest(
        String accountId,
        ProfileRequest profile
){}