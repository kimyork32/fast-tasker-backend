package com.fasttasker.fast_tasker.config;

import com.fasttasker.fast_tasker.application.AccountService;
import com.fasttasker.fast_tasker.application.TaskService;
import com.fasttasker.fast_tasker.application.TaskerService;
import com.fasttasker.fast_tasker.application.dto.account.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.account.RegisterAccountRequest;
import com.fasttasker.fast_tasker.application.dto.task.OfferRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.ProfileRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.domain.account.*;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
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
    private final AccountService accountService;
    private final TaskerService taskerService;

    public DataSeeder(ITaskerRepository userRepository, IAccountRepository accountRepository, TaskService taskService, PasswordEncoder passwordEncoder, AccountService accountService, TaskerService taskerService) {
        this.taskerRepository = userRepository;
        this.accountRepository = accountRepository;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.taskerService = taskerService;
    }

    @Override
    public void run(String... args) throws Exception {

        var registerAccountRequest1 = new RegisterAccountRequest("gato@gmail.com", "123");
        var registerAccountRequest2 = new RegisterAccountRequest("tito@gmail.com", "123");

        AccountResponse accountResponse1 = accountService.registerAccount(registerAccountRequest1);
        AccountResponse accountResponse2 = accountService.registerAccount(registerAccountRequest2);

        var taskerRequest1 = TaskerRequest.builder()
                .accountId(accountResponse1.id())
                .profile(ProfileRequest.builder()
                        .firstName("gato")
                        .lastName("fronton")
                        .photo("")
                        .about("sobre mi")
                        .reputation(3)
                        .clientReviews(0)
                        .completedTasks(0)
                        .location(LocationRequest.builder()
                                .latitude(23.33)
                                .longitude(525.23)
                                .address("address 1")
                                .zip(4402)
                                .build()
                                )
                        .build())
                .build();

        var taskerRequest2 = taskerRequest1.toBuilder()
                .accountId(accountResponse2.id())
                .profile(taskerRequest1.profile().toBuilder()
                        .firstName("tito")
                        .build())
                .build();

        taskerService.registerTasker(taskerRequest1);
        taskerService.registerTasker(taskerRequest2);


        // simulating that the tasker creating a task
        var taskRequest = TaskRequest.builder()
                .title("mover mi coche")
                .description("quiero mover esto afuera de mi casa")
                .budget(54)
                .location(LocationRequest.builder()
                        .latitude(134.234)
                        .longitude(345.42)
                        .address("miraflores porvenir N21")
                        .build())
                .taskDate("2025-12-10")
                .build();

        TaskResponse taskResponse = taskService.createTask(taskRequest, UUID.fromString(accountResponse1.id()));


        // simulating that the tasker 2 creates an offer
        var offerRequest = new OfferRequest(
                44,
                "quiero hacer esto"
        );
        taskService.createOffer(offerRequest, UUID.fromString(taskResponse.id()), UUID.fromString(accountResponse2.id()));
    }
}