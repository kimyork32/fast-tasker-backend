package com.fasttasker.fast_tasker.application.dto;

public record RegisterAccountRequest(
        String email,
        String rawPassword
) {}
