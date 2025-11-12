package com.fasttasker.fast_tasker.application.dto;

import com.fasttasker.fast_tasker.domain.account.AccountStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountResponse(
        UUID id,
        String email,
        AccountStatus status
) {}
