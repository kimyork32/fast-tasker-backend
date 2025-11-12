package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.AccountService;
import com.fasttasker.fast_tasker.application.dto.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.LoginRequest;
import com.fasttasker.fast_tasker.application.dto.RegisterAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * constructor for dependencies injection
     * @param accountService service
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     *
     * @param request the DTO {@link RegisterAccountRequest} that contain email and rawPassword
     * @return an {@link ResponseEntity} that contain the DTO {@link AccountResponse}
     * of the newly created user and  HTTP status code {@code 201 Created}
     */
    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody RegisterAccountRequest request) {
        AccountResponse response = accountService.registerAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     *
     * @param request the DTO {@link ResponseEntity} that contain email and rawPassword
     * @return an {@link ResponseEntity} that contain the DTO {@link AccountResponse}
     * of the newly created user and  HTTP status code {@code 201 OK}
     */
    @PostMapping("/login")
    public ResponseEntity<AccountResponse> login(@RequestBody LoginRequest request) {
        AccountResponse response = accountService.login(request.email(), request.rawPassword());
        return ResponseEntity.ok(response);
    }
}