package com.fasttasker.fast_tasker.application.service;

import com.fasttasker.fast_tasker.application.dto.account.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.account.LoginResponse;
import com.fasttasker.fast_tasker.application.dto.account.RegisterAccountRequest;
import com.fasttasker.fast_tasker.application.exception.AccountNotFoundException;
import com.fasttasker.fast_tasker.application.exception.EmailAlreadyExistsException;
import com.fasttasker.fast_tasker.application.exception.PasswordIncorrectException;
import com.fasttasker.fast_tasker.application.exception.TaskerNotFoundException;
import com.fasttasker.fast_tasker.application.mapper.AccountMapper;
import com.fasttasker.fast_tasker.config.JwtService;
import com.fasttasker.fast_tasker.domain.account.*;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

/**
 *
 */
@Slf4j
@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final ITaskerRepository taskerRepository;
    private final ITaskRepository taskRepository;
    private final AccountMapper accountMapper;
    private final NotificationService notificationService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     *
     */
    public AccountService(
            IAccountRepository accountRepository,
            ITaskerRepository taskerRepository,
            ITaskRepository taskRepository,
            AccountMapper accountMapper, NotificationService notificationService,
            PasswordEncoder passwordEncoder, JwtService jwtService
    ) {
        this.accountRepository = accountRepository;
        this.taskerRepository = taskerRepository;
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     *
     * @param request
     * @return
     */
    @Transactional
    public AccountResponse registerAccount(RegisterAccountRequest request) {
        accountRepository.findByEmailValue(request.email()).ifPresent(acc -> {
            throw new EmailAlreadyExistsException(
                    "the email address" + request.email() + " is already in use");
        });

        // the password is receiver of the client in plain text. The client will send
        // it using a secure channel such as HTTPS.
        String hashedPassword = passwordEncoder.encode(request.rawPassword());

        var account = new Account(
                UUID.randomUUID(),  // probability of generating two identical id: 1 / 2^122
                new Email(request.email()),
                new Password(hashedPassword),
                AccountStatus.PENDING_VERIFICATION
        );

        // save account, if any error occurs then rollback
        accountRepository.save(account);

        var defaultTasker = Tasker.createWithoutProfile(account.getTaskerId());

        // save tasker, if any error occurs then rollback
        taskerRepository.save(defaultTasker);

        // notifying of the tasker that your account has been created
        notificationService.sendNotification(defaultTasker.getId(), null, NotificationType.SYSTEM);

        return accountMapper.toResponse(account);
    }

    /**
     *
     * @param email
     * @param rawPassword
     * @return
     */
    @Transactional(readOnly = true)
    public LoginResponse login(String email, String rawPassword) {
        Account account = accountRepository.findByEmailValue(email)
                .orElseThrow(() -> new AccountNotFoundException("login exception: invalid email"));

        if (!passwordEncoder.matches(rawPassword, account.getPasswordHash().getValue())) {
            throw new AccountNotFoundException("login exception: invalid password");
        }

        if (account.getStatus() == AccountStatus.BANNED) {
            throw new AccountNotFoundException("your account has been banned");
        }

        // NOTE: This is inefficient
        Tasker tasker = taskerRepository.findByAccountId(account.getTaskerId())
                .orElseThrow(() -> new TaskerNotFoundException("Tasker not found with account id: " + account.getTaskerId()));

        // calculate if the profile is complete.
        // NOTE: This should be factored out
        boolean profileCompleted = tasker.getProfile().getFirstName() != null && !tasker.getProfile().getFirstName().isBlank()
                && tasker.getProfile().getLastName() != null && !tasker.getProfile().getLastName().isBlank();

        log.info("profileCompleted: {}", profileCompleted);

        // return a JWT token
        String token = jwtService.generateToken(account.getTaskerId(), profileCompleted);
        return new LoginResponse(token);
    }

    /**
     *
     */
    @Transactional
    public void changePassword(UUID accountId, String oldPass, String newPass) {
        Account account = findAccountById(accountId);

        if (!passwordEncoder.matches(oldPass, account.getPasswordHash().getValue())) {
            throw new PasswordIncorrectException("the password has been incorrect");
        }

        String newHashedPassword = passwordEncoder.encode(newPass);
        account.setPasswordHash(new Password(newHashedPassword));
        accountRepository.save(account);
    }

    /**
     *
     */
    @Transactional
    public void activate(UUID accountId) {
        Account account = findAccountById(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    /**
     *
     */
    @Transactional
    public void ban(UUID accountId) {
        Account account = findAccountById(accountId);
        account.setStatus(AccountStatus.BANNED);
        accountRepository.save(account);

        List<Task> userTasks = taskRepository.findByPosterIdAndStatus(accountId, TaskStatus.ACTIVE);
        for (Task task : userTasks) {
            task.setStatus(TaskStatus.CANCELLED);
        }
        taskRepository.saveAll(userTasks);
    }

    /**
     *
     */
    @Transactional(readOnly = true)
    public AccountResponse getById(UUID accountId) {
        Account account = findAccountById(accountId);
        return accountMapper.toResponse(account);
    }

    /**
     *
     */
    private Account findAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("not found account with id: " + accountId));
    }
}