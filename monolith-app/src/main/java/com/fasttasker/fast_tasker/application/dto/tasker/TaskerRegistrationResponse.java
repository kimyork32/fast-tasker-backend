package com.fasttasker.fast_tasker.application.dto.tasker;

public record TaskerRegistrationResponse(
        String id,
        String accountId,
        ProfileResponse profile,
        String token
) {}
