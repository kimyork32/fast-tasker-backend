package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.service.NotificationService;
import com.fasttasker.fast_tasker.application.service.TaskerService;
import com.fasttasker.fast_tasker.application.dto.notification.NotificationResponse;
import com.fasttasker.fast_tasker.application.dto.task.AssignTaskerRequest;
import com.fasttasker.fast_tasker.application.dto.task.AssignTaskerResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRegistrationResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.config.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 *
 */
@RestController
@RequestMapping("api/v1/tasker")
public class TaskerController {

    private final TaskerService taskerService;
    private final NotificationService notificationService;
    private final JwtService jwtService;

    /**
     * constructor for dependencies injection
     */
    @Autowired
    public TaskerController(TaskerService taskerService, NotificationService notificationService, JwtService jwtService) {
        this.taskerService = taskerService;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @PutMapping("/register")
    public ResponseEntity<TaskerRegistrationResponse> initialRegister(
            @RequestBody TaskerRequest request,
            Authentication authentication
    ) {

        UUID accountId = (UUID) authentication.getPrincipal();
        var serviceRequest = new TaskerRequest(accountId.toString(), request.profile());
        TaskerResponse taskerResponse = taskerService.registerTasker(serviceRequest);
        String newToken = jwtService.generateToken(accountId, true); // profileCompleted = true
        // creating DTO with the token
        var response = new TaskerRegistrationResponse(
                taskerResponse.id(),
                taskerResponse.accountId(),
                taskerResponse.profile(),
                newToken
        );

        // return profile and new token
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<TaskerResponse> getTaskerById(@PathVariable UUID userId) {
        TaskerResponse response = taskerService.getById((userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/me")
    public ResponseEntity<TaskerResponse> getTaskerMe(Authentication authentication) {
        // first extract the Principal, before casting the object to UUID
        UUID taskerId = (UUID) authentication.getPrincipal();
        // { @see JwtAuthenticationFilter } for explanation
        TaskerResponse response = taskerService.getByAccountId(taskerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/assign-tasker")
    public ResponseEntity<AssignTaskerResponse> assignTasker(
            Authentication authentication,
            @Valid @RequestBody AssignTaskerRequest request
    ) {
        UUID posterId = (UUID) authentication.getPrincipal();
        AssignTaskerResponse response = taskerService.assignTaskToTasker(request, posterId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByTasker(Authentication authentication) {
        UUID taskerId = (UUID) authentication.getPrincipal();
        List<NotificationResponse> notifications = notificationService.getAll(taskerId);
        return ResponseEntity.ok(notifications);
    }
}
