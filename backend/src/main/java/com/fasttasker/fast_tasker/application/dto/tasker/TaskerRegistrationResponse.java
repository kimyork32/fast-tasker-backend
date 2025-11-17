package com.fasttasker.fast_tasker.application.dto.tasker;

import java.util.UUID;

public record TaskerRegistrationResponse(
        UUID id,
        UUID accountId,
        ProfileResponse profile,
        String token
) {}
