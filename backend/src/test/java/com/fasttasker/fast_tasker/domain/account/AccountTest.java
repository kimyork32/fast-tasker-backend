package com.fasttasker.fast_tasker.domain.account;

import com.fasttasker.fast_tasker.application.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    private Email validEmail;
    private Password validPassword;

    @BeforeEach
    void setUp() {
        validEmail = new Email("user@fasttasker.com");
        validPassword = new Password("hashed_password_123");
    }

    @Test
    @DisplayName("Should create new Account with UUID and PENDING status")
    void shouldCreateNewAccount() {
        Account account = new Account(validEmail, validPassword);

        assertThat(account.getId()).isNotNull(); // UUID generated
        assertThat(account.getStatus()).isEqualTo(AccountStatus.PENDING_VERIFICATION);
        assertThat(account.getEmail()).isEqualTo(validEmail);
        assertThat(account.getPassword()).isEqualTo(validPassword);
    }

    @Test
    @DisplayName("Should throw exception if constructor parameters are null")
    void shouldThrowExceptionIfNullConstructorArgs() {
        assertThatThrownBy(() -> new Account(null, validPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email cannot be null");

        assertThatThrownBy(() -> new Account(validEmail, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password cannot be null");
    }

    @Test
    @DisplayName("changePassword: Should update password successfully")
    void shouldChangePassword() {
        Account account = new Account(validEmail, validPassword);
        Password newPassword = new Password("new_hashed_password_456");

        account.changePassword(newPassword);

        assertThat(account.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("changePassword: Should throw DomainException if new password is null")
    void shouldThrowExceptionWhenChangingToNullPassword() {
        Account account = new Account(validEmail, validPassword);

        assertThatThrownBy(() -> account.changePassword(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Password cannot be null");
    }

    @Test
    @DisplayName("changeStatus: Should update account status")
    void shouldChangeStatus() {
        Account account = new Account(validEmail, validPassword);

        account.changeStatus(AccountStatus.ACTIVE);

        assertThat(account.getStatus()).isEqualTo(AccountStatus.ACTIVE);
    }
}