package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.account.Account;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepositoryImpl implements IAccountRepository {

    private final JpaAccountRepository jpa;

    public AccountRepositoryImpl(JpaAccountRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Account save(Account account) {
        return jpa.save(account);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Account> findByEmailValue(String emailValue) {
        return jpa.findByEmailValue(emailValue);
    }
}
