package com.fasttasker.fast_tasker.persistence.jpa;

import com.fasttasker.fast_tasker.domain.account.*;
import com.fasttasker.fast_tasker.persistence.repository.AccountRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AccountRepositoryImpl.class))
class JpaAccountRepositoryTest {

    Logger logger = Logger.getLogger(JpaAccountRepositoryTest.class.getName());

    @Autowired
    private IAccountRepository accountRepository;

    @Test
    void shouldSaveAndFindYourAccountByEmail() {
        // 1. ARRANGE
        UUID id = UUID.randomUUID();
        Email email = new Email("test@domain.com");
        Password hash = new Password("bcrypt-hash-string-12345");
        logger.info("expected:");
        logger.info("id: " + id);
        logger.info("email: " + email.getValue());
        logger.info("hashed password: " + hash.getValue());

        Account newAccount = new Account(
                email,
                hash
        );
        logger.info(newAccount.toString()); // automatic toString

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
        assertThat(accountFound.getPassword()).isEqualTo(hash);
        assertThat(accountFound.getStatus()).isEqualTo(AccountStatus.PENDING_VERIFICATION);
    }

    @Test
    void shouldSaveAndFindYourAccountById() {
        // 1. ARRANGE
        UUID id = UUID.randomUUID();
        Email email = new Email("test2@domain.com");
        Password hash = new Password("bcrypt-hash-string-313131");
        Account newAccount = new Account(
                email,
                hash
        );

        // 2. ACT
        // save account in the test bd
        accountRepository.save(newAccount);
        // we tried find it with ID use the repository method
        Optional<Account> accountFoundOpt = accountRepository.findById(id);

        // 3. ASSERT
        // verify if it was found
        assertThat(accountFoundOpt).isPresent();
        Account accountFound = accountFoundOpt.get();

        // checking if it is equal to what was expected
        assertThat(accountFound.getEmail()).isEqualTo(email);
    }
}