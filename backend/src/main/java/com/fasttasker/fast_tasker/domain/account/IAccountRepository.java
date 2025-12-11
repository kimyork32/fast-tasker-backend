package com.fasttasker.fast_tasker.domain.account;

import java.util.UUID;

/**
 * repository por for the account entity.
 * define the contract that the application layer needs.
 */
public interface IAccountRepository {

    Account save(Account account);

    Account findById(UUID id);

    boolean existsByEmailValue(String emailValue);

    Account getByEmailValue(String emailValue);

}