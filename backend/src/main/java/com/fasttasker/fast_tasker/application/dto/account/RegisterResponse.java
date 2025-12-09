package com.fasttasker.fast_tasker.application.dto.account;

import java.util.UUID;

public record RegisterResponse(
        String id,
        String email,
        String token
) {}
