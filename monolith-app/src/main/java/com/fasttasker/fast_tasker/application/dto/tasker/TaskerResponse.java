package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

@Builder
public record TaskerResponse(
    String id,
    String accountId,
    ProfileResponse profile
) {}