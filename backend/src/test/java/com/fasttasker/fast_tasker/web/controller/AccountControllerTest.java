package com.fasttasker.fast_tasker.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasttasker.fast_tasker.application.AccountService;
import com.fasttasker.fast_tasker.application.dto.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.RegisterAccountRequest;
import com.fasttasker.fast_tasker.domain.account.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class,
            excludeAutoConfiguration = {SecurityAutoConfiguration.class}) // only for this tests
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc; // for simulate HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // helper for convert DTO to JSON

    @MockitoBean
    private AccountService accountService;

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        // 1. GIVEN
        // DTO that will send in the body for the request
        var requestDTO = new RegisterAccountRequest(
                "new-user33@domain.com",
                "password321"
        );

        // the simulated DTO that the service will return
        var responseDTO = new AccountResponse(
                UUID.randomUUID(),
                "new-user33@domain.com",
                AccountStatus.PENDING_VERIFICATION
        );

        // mock
        // WHEN accountService.registerAccount is call with ANY DTO ...
        when(accountService.registerAccount(any(RegisterAccountRequest.class)))
                .thenReturn(responseDTO); // THEN return responseDTO

        // 2. WHEN

        // mockMvc.perform() simulate the HTTP requests. Similar to POSTMAN)
        mockMvc.perform(
                post("/api/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON) // set header how 'Content-Type'
                        .content(objectMapper.writeValueAsString(requestDTO)) // body
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.id().toString()))
                .andExpect(jsonPath("$.email").value("new-user33@domain.com"))
                .andExpect(jsonPath("$.status").value("PENDING_VERIFICATION"));
    }
}