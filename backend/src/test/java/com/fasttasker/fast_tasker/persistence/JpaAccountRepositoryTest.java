package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.account.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaAccountRepositoryTest {

    @Autowired
    private IAccountRepository accountRepository;

    @Test
    void shouldSaveAndFindYourAccountByEmail() {
        // 1. ARRANGE
        UUID id = UUID.randomUUID();
        Email email = new Email("test@domain.com");
        Password hash = new Password("bcrypt-hash-string-12345");

        Account newAccount = new Account(
                id,
                email,
                hash,
                AccountStatus.PENDING_VERIFICATION
        );

        // 2. ACT
        // save account in the test bd
        accountRepository.save(newAccount);
        // we tried to find it using the repository method
        Optional<Account> accountFoundOpt = accountRepository.findByEmailValue("test@domain.com");

        // 3. ASSERT
        // a) checking that we found it
        assertThat(accountFoundOpt).isPresent();

        // b) checking that the data is correct
        Account accountFound = accountFoundOpt.get();

        assertThat(accountFound.getEmail()).isEqualTo(email);
        assertThat(accountFound.getPasswordHash()).isEqualTo(hash);
        assertThat(accountFound.getStatus()).isEqualTo(AccountStatus.PENDING_VERIFICATION);
    }
}