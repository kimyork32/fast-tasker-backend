package com.fasttasker.fast_tasker.application.dto.tasker;

import java.util.UUID;

public record TaskerRequest(
        UUID accountId,
        ProfileRequest profile
){}