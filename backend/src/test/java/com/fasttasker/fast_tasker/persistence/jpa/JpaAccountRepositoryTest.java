package com.fasttasker.fast_tasker.persistence.jpa;

import com.fasttasker.fast_tasker.domain.account.Account;
import com.fasttasker.fast_tasker.domain.account.Email;
import com.fasttasker.fast_tasker.domain.account.Password;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test
 */
@DataJpaTest
class JpaAccountRepositoryTest {

    @Autowired
    private JpaAccountRepository jpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindByEmailValue() {
        // 1. Arrange
        String emailValue = "juan@fasttasker.com";
        Account account = createAccountEntity(emailValue);

        entityManager.persist(account);
        entityManager.flush();

        // 2. Act
        Optional<Account> found = jpaRepository.findByEmailValue(emailValue);

        // 3. Assert
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getEmail().getValue()).isEqualTo(emailValue);
    }

    @Test
    void shouldCheckIfEmailExists() {
        // 1. Arrange
        String emailValue = "exists@fasttasker.com";
        Account account = createAccountEntity(emailValue);

        entityManager.persist(account);
        entityManager.flush();

        // 2. Act
        boolean exists = jpaRepository.existsByEmailValue(emailValue);

        // 3. Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = jpaRepository.existsByEmailValue("unknown@fasttasker.com");
        assertThat(exists).isFalse();
    }

    // helper
    private Account createAccountEntity(String emailString) {
        Email emailVO = new Email(emailString);
        Password passwordVO = new Password("hashed_secret_123");

        return new Account(emailVO, passwordVO);
    }
}