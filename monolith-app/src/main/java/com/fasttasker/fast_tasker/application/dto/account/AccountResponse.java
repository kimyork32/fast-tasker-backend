package com.fasttasker.fast_tasker.application.dto.account;

import com.fasttasker.fast_tasker.domain.account.AccountStatus;
import lombok.Builder;

@Builder
public record AccountResponse(
        String id,
        String email,
        AccountStatus status
) {}
