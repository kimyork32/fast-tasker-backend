package com.fasttasker.fast_tasker.domain.account;

import java.util.Optional;
import java.util.UUID;

/**
 * repository por for the account entity.
 * define the contract that the application layer needs.
 */
public interface IAccountRepository {

    Account save(Account account);

    Optional<Account> findById(UUID id);

    Optional<Account> findByEmailValue(String emailValue);
}