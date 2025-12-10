package com.fasttasker.fast_tasker.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasttasker.fast_tasker.application.service.AccountService;
import com.fasttasker.fast_tasker.application.dto.account.AccountResponse;
import com.fasttasker.fast_tasker.application.dto.account.RegisterAccountRequest;
import com.fasttasker.fast_tasker.config.JwtService;
import com.fasttasker.fast_tasker.domain.account.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtService jwtService;

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        // 1. GIVEN
        var requestDTO = new RegisterAccountRequest(
                "new-user33@domain.com",
                "password321"
        );

        var responseDTO = new AccountResponse(
                UUID.randomUUID().toString(),
                "new-user33@domain.com",
                AccountStatus.PENDING_VERIFICATION
        );

        when(accountService.registerAccount(any(RegisterAccountRequest.class)))
                .thenReturn(responseDTO);

        // return any token for any accountId
        when(jwtService.generateToken(any(UUID.class), any(Boolean.class)))
                .thenReturn("mocked.jwt.token");

        // 2. WHEN & THEN
        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.id()))
                .andExpect(jsonPath("$.email").value(responseDTO.email())) // now verify the new DTO structure
                .andExpect(jsonPath("$.token").value(notNullValue())); // this too
    }
}