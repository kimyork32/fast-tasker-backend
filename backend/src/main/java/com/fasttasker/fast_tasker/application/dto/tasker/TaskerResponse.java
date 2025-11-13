package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TaskerResponse(
    UUID id,
    UUID accountId,
    ProfileResponse profile
) {}