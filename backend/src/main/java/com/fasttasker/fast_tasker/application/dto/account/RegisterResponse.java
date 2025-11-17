package com.fasttasker.fast_tasker.application.dto.account;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String email,
        String token
) {}
