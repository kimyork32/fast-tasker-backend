package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.AccountService;
import com.fasttasker.fast_tasker.application.dto.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.LoginRequest;
import com.fasttasker.fast_tasker.application.dto.LoginResponse;
import com.fasttasker.fast_tasker.application.dto.RegisterAccountRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@RestController
@RequestMapping("api/v1/auth")
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
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse servletResponse
    ) {
        LoginResponse loginResponse = accountService.login(request.email(), request.rawPassword());

        // adapting this: https://javascript.plainenglish.io/nextjs-authentication-flow-store-jwt-in-cookie-fa6e6c8c0dca
        // creating the HTTPOnly cookie
        ResponseCookie cookie = ResponseCookie.from("jwtToken", loginResponse.token())
                .httpOnly(true) // javascript cannot read it
                .secure(false) // IMPORTANT: if to use HTTPS in production, set to true
                .path("/") // available for all routes
                .maxAge(60 * 60 * 24) // 1 day, this MATCHES with the value of {@code jwt.expiration}
                                                    // in application.properties
                .build();

        // add the cookie to the response header
        servletResponse.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(loginResponse);
    }
}