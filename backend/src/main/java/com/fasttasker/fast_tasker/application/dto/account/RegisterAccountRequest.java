package com.fasttasker.fast_tasker.application.dto.account;

public record RegisterAccountRequest(
        String email,
        String rawPassword
) {}
