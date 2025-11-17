package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.account.AccountResponse;
import com.fasttasker.fast_tasker.domain.account.Account;
import org.springframework.stereotype.Component;

/**
 * builder pattern
 * NOTE: not necessary for this because it has few parameters for build it, but it can be scalable
 */
@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {
        if (account == null) return null;

        return AccountResponse.builder()
                .id(account.getTaskerId())
                .email(account.getEmail().getValue())
                .status(account.getStatus())
                .build();
    }
}
