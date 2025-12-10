package com.fasttasker.fast_tasker.application.dto.account;

public record RegisterResponse(
        String id,
        String email,
        String token
) {}
