package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.TaskerService;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRegistrationResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 *
 */
@RestController
@RequestMapping("api/v1/tasker")
public class TaskerController {

    private final TaskerService taskerService;
    private final JwtService jwtService;

    /**
     * constructor for dependencies injection
     */
    @Autowired
    public TaskerController(TaskerService taskerService, JwtService jwtService) {
        this.taskerService = taskerService;
        this.jwtService = jwtService;
    }

    @PutMapping("/register")
    public ResponseEntity<TaskerRegistrationResponse> initialRegister(
            @RequestBody TaskerRequest request,
            Authentication authentication
    ) {

        UUID accountId = (UUID) authentication.getPrincipal();
        var serviceRequest = new TaskerRequest(accountId, request.profile());
        TaskerResponse taskerResponse = taskerService.registerTasker(serviceRequest, accountId);
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
}
