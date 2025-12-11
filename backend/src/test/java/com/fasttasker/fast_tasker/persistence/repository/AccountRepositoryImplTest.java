package com.fasttasker.fast_tasker.persistence.repository;

import com.fasttasker.fast_tasker.application.exception.AccountNotFoundException;
import com.fasttasker.fast_tasker.domain.account.Account;
import com.fasttasker.fast_tasker.persistence.jpa.JpaAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

    @Mock
    private JpaAccountRepository jpaRepository;

    @InjectMocks
    private AccountRepositoryImpl accountRepository;

    @Test
    void shouldSaveAccount() {
        // Arrange
        Account account = mock(Account.class);
        when(jpaRepository.save(account)).thenReturn(account);

        // Act
        Account result = accountRepository.save(account);

        // Assert
        assertThat(result).isEqualTo(account);
        verify(jpaRepository).save(account);
    }

    @Test
    void shouldReturnAccountWhenIdFound() {
        UUID id = UUID.randomUUID();
        Account expectedAccount = mock(Account.class);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(expectedAccount));

        Account result = accountRepository.findById(id);

        assertThat(result).isEqualTo(expectedAccount);
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountRepository.findById(id))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        String email = "test@fasttasker.com";
        when(jpaRepository.existsByEmailValue(email)).thenReturn(true);

        boolean result = accountRepository.existsByEmailValue(email);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnAccountWhenEmailFound() {
        String email = "test@fasttasker.com";
        Account expectedAccount = mock(Account.class);

        when(jpaRepository.findByEmailValue(email)).thenReturn(Optional.of(expectedAccount));

        Account result = accountRepository.getByEmailValue(email);

        assertThat(result).isEqualTo(expectedAccount);
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        String email = "missing@fasttasker.com";

        when(jpaRepository.findByEmailValue(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountRepository.getByEmailValue(email))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining(email);
    }
}