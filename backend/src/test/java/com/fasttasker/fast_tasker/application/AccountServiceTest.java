package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.account.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.account.LoginResponse;
import com.fasttasker.fast_tasker.application.dto.account.RegisterAccountRequest;
import com.fasttasker.fast_tasker.application.mapper.AccountMapper;
import com.fasttasker.fast_tasker.application.service.AccountService;
import com.fasttasker.fast_tasker.application.service.NotificationService;
import com.fasttasker.fast_tasker.config.JwtService;
import com.fasttasker.fast_tasker.domain.account.*;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private ITaskerRepository taskerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldRegisterAccountSuccess() {
        // 1. GIVEN
        var request = new RegisterAccountRequest(
                "newUser@domain.com",
                "password123456"
        );
        String hashedPassword = "hashedPassword-xyz";
        var responseDto = new AccountResponse(
                UUID.randomUUID().toString(),
                request.email(),
                AccountStatus.PENDING_VERIFICATION
        );

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<Tasker> taskerCaptor = ArgumentCaptor.forClass(Tasker.class);

        when(accountRepository.findByEmailValue(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.rawPassword())).thenReturn(hashedPassword);
        when(accountMapper.toResponse(any(Account.class))).thenReturn(responseDto);

        // 2. WHEN
        AccountResponse response = accountService.registerAccount(request);

        // 3. THEN
        verify(accountRepository).findByEmailValue("newUser@domain.com");
        verify(passwordEncoder).encode(request.rawPassword());

        verify(accountRepository).save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();
        assertThat(savedAccount.getEmail().getValue()).isEqualTo(request.email());
        assertThat(savedAccount.getPasswordHash().getValue()).isEqualTo(hashedPassword);
        assertThat(savedAccount.getStatus()).isEqualTo(AccountStatus.PENDING_VERIFICATION);
        assertThat(savedAccount.getTaskerId()).isNotNull();

        verify(taskerRepository).save(taskerCaptor.capture());
        Tasker savedTasker = taskerCaptor.getValue();
        assertThat(savedTasker.getAccountId()).isEqualTo(savedAccount.getTaskerId());
        assertThat(savedTasker.getProfile()).isNull();

        verify(notificationService).sendNotification(eq(savedTasker.getId()), isNull(), eq(NotificationType.SYSTEM));

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.email()).isEqualTo("newUser@domain.com");
        assertThat(response.status()).isEqualTo(AccountStatus.PENDING_VERIFICATION);
    }

    @Test
    void shouldLoginSuccess() {
        // 1. GIVE

        // simulating an existing account
        String email = "old-user@domain.com";
        String rawPassword = "password123456";
        String hashedPassword = "hashedPassword-xyz";
        UUID accountId = UUID.randomUUID();

        var accountToFind = new Account(
                accountId,
                new Email(email),
                new Password(hashedPassword),
                AccountStatus.ACTIVE
        );

        String fakeToken= "fake-token.eyJzdWIiOiJ";

        var  fakeLocation = new Location(
                -15.542353,
                -12.252514,
                "address fake",
                4141414
        );

        var fakeProfile = new Profile(
                "fakundo",
                "gonzales",
                "photo.com",
                fakeLocation,
                "about me",
                3,
                5,
                12
        );

        var taskerSaved = new Tasker(
                UUID.randomUUID(),
                accountId,
                fakeProfile
        );

        // simulating that the tasker EXISTS
        when(taskerRepository.findById(accountToFind.getTaskerId()))
                .thenReturn(Optional.of(taskerSaved));

        // simulating that the email EXISTS
        when(accountRepository.findByEmailValue(email))
                .thenReturn(Optional.of(accountToFind));

        // simulating the hashing of the password
        when(passwordEncoder.matches(rawPassword, hashedPassword))
                .thenReturn(true);

        // simulating the token generation to return the fake
        when(jwtService.generateToken(accountId, true)).thenReturn(fakeToken);
        // 2. WHEN
        LoginResponse response = accountService.login(email, rawPassword);

        // 3. THEN
        // verify that the email was found
        verify(accountRepository).findByEmailValue(email);

        // verify that the password was hashed
        verify(passwordEncoder).matches(rawPassword, hashedPassword);

        // verifiy that generate token was called with correct ID
        verify(jwtService).generateToken(accountId, true);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(fakeToken);
    }
}