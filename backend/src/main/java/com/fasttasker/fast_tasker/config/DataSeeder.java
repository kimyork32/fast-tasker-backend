package com.fasttasker.fast_tasker.config;


import com.fasttasker.fast_tasker.application.TaskService;
import com.fasttasker.fast_tasker.application.dto.task.OfferRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.domain.account.*;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ITaskerRepository taskerRepository;
    private final IAccountRepository accountRepository;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(ITaskerRepository userRepository, IAccountRepository accountRepository, TaskService taskService, PasswordEncoder passwordEncoder) {
        this.taskerRepository = userRepository;
        this.accountRepository = accountRepository;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Account account1 = Account.builder()
                .taskerId(UUID.randomUUID())
                .email(new Email("gato@gmail.com"))
                .passwordHash(new Password(passwordEncoder.encode("123")))
                .status(AccountStatus.ACTIVE)
                .build();

        Tasker tasker1 = Tasker.builder()
                .id(account1.getTaskerId())
                .accountId(account1.getTaskerId())
                .profile(Profile.builder()
                        .firstName("gato")
                        .lastName("fronton")
                        .location(new Location(
                                0.0,
                                0.0,
                                "miraflores, miraflores",
                                41411
                        ))
                        .about("about me")
                        .reputation(0)
                        .clientReviews(0)
                        .completedTasks(0)
                        .build())
                .build();

        Account account2 = account1.toBuilder()
                .taskerId(UUID.randomUUID())
                .email(new Email("tito@gmail.com"))
                .build();

        Tasker tasker2 = tasker1.toBuilder()
                .id(account2.getTaskerId())
                .accountId(account2.getTaskerId())
                .profile(tasker1.getProfile().toBuilder()
                        .firstName("tito")
                        .build())
                .build();


        accountRepository.save(account1);
        accountRepository.save(account2);

        taskerRepository.save(tasker1);
        taskerRepository.save(tasker2);

        // creando task
        var taskRequest = new TaskRequest(
                "mover cama",
                "quiero mover esto",
                44,
                new LocationRequest(123.145, 123.12, "miraflores", 424),
                "2023-10-26"
        );

        TaskResponse taskResponse = taskService.createTask(taskRequest, account2.getTaskerId());


        // creando oferta
        var offerRequest = new OfferRequest(
                44,
                "quiero hacer esto"
        );
        taskService.createOffer(offerRequest, UUID.fromString(taskResponse.id()), account2.getTaskerId());
    }
}