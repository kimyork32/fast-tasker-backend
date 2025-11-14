package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.TaskerService;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
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

    /**
     * constructor for dependencies injection
     */
    @Autowired
    public TaskerController(TaskerService taskerService) {
        this.taskerService = taskerService;
    }

    @PutMapping("/register")
    public ResponseEntity<TaskerResponse> initialRegister(@RequestBody TaskerRequest request) {

        TaskerResponse response = taskerService.registerTasker(request);
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
        UUID accountId = (UUID) authentication.getPrincipal();

        // { @see JwtAuthenticationFilter } for explanation
        TaskerResponse response = taskerService.getByAccountId(accountId);

        return ResponseEntity.ok(response);
    }
}
