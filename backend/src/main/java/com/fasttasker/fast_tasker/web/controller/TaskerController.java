package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.TaskerService;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final String AUTH_COOKIE_NAME = "jwtToken"; // this should be same as proxy.ts token

    /**
     * constructor for dependencies injection
     */
    @Autowired
    public TaskerController(TaskerService taskerService, JwtService jwtService) {
        this.taskerService = taskerService;
        this.jwtService = jwtService;
    }

    @PutMapping("/register")
    public ResponseEntity<TaskerResponse> initialRegister(
            @RequestBody TaskerRequest request,
            Authentication authentication
    ) {

        UUID accountId = (UUID) authentication.getPrincipal();
        request = new TaskerRequest(accountId, request.profile());

        TaskerResponse response = taskerService.registerTasker(request);

        String newToken = jwtService.generateToken(accountId, true); // profileCompleted = true

        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, newToken)
                .httpOnly(false)
                .secure(false) // enable this in production
                .path("/")
                .maxAge(60 * 60 * 24) // 1 day
                .build();

        // return profile and new token
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
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

        System.out.println(response);

        return ResponseEntity.ok(response);
    }
}
