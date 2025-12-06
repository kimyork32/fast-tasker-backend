package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.account.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.account.LoginResponse;
import com.fasttasker.fast_tasker.application.dto.account.RegisterAccountRequest;
import com.fasttasker.fast_tasker.application.exception.AccountNotFoundException;
import com.fasttasker.fast_tasker.application.exception.EmailAlreadyExistsException;
import com.fasttasker.fast_tasker.application.exception.TaskerNotFoundException;
import com.fasttasker.fast_tasker.application.mapper.AccountMapper;
import com.fasttasker.fast_tasker.config.JwtService;
import com.fasttasker.fast_tasker.domain.account.*;
import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.notification.Notification;
import com.fasttasker.fast_tasker.domain.notification.NotificationStatus;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Service
public class AccountService {

    private final IAccountRepository accountRepository;
    private final ITaskerRepository taskerRepository;
    private final ITaskRepository taskRepository;
    private final INotificationRepository notificationRepository;
    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     *
     */
    public AccountService(
            IAccountRepository accountRepository,
            ITaskerRepository taskerRepository,
            ITaskRepository taskRepository,
            INotificationRepository notificationRepository, AccountMapper accountMapper,
            PasswordEncoder passwordEncoder, JwtService jwtService
    ) {
        this.accountRepository = accountRepository;
        this.taskerRepository = taskerRepository;
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
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

        var defaultLocation = new Location(
                0,
                0,
                "",
                0
        );

        var defaultProfile = new Profile(
                "",
                "",
                "",
                defaultLocation,
                "",
                0,
                0,
                0
        );

        var tasker = new Tasker(
                account.getTaskerId(),
                account.getTaskerId(),
                defaultProfile
        );

        // save account, if any error occurs then rollback
        taskerRepository.save(tasker);

        Notification welcomeNotification = new Notification(
                UUID.randomUUID(),
                account.getTaskerId(),
                NotificationType.SYSTEM,
                NotificationStatus.UNREAD
        );

        // save notification, if any error occurs then rollback
        notificationRepository.save(welcomeNotification);

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
        Tasker tasker = taskerRepository.findById(account.getTaskerId())
                .orElseThrow(() -> new TaskerNotFoundException("Tasker not found"));

        // calculate if the profile is complete.
        // NOTE: This should be factored out
        boolean profileCompleted = tasker.getProfile().getFirstName() != null && !tasker.getProfile().getFirstName().isBlank()
                && tasker.getProfile().getLastName() != null && !tasker.getProfile().getLastName().isBlank();

        System.out.println("AccountService. login. profileCompleted: " + profileCompleted);

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
            throw new RuntimeException("the password has been incorrect");
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