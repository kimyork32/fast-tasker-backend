package com.fasttasker.fast_tasker.application.dto;

import com.fasttasker.fast_tasker.domain.account.AccountStatus;

import java.util.UUID;

public record AccountResponse(
        UUID id,
        String email,
        AccountStatus status
) {}
