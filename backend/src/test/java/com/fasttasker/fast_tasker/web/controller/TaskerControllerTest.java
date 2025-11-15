package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.TaskerService;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.ProfileResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.config.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * integration test
 */

@SpringBootTest
@AutoConfigureMockMvc
class TaskerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private TaskerService taskerService;

    @Test
    void shouldGetTaskerMeWhenAuthenticated() throws Exception {
        UUID testAccountId = UUID.randomUUID();
        String validToken = jwtService.generateToken(testAccountId, true);

        var mockLocation = new LocationResponse(
                -13.412453,
                -12.158023,
                "adress street"
        );

        var mockProfile = new ProfileResponse(
                "homer",
                "simpson",
                "https://amazon.com/photo/235",
                "about me",
                4,
                14,
                12,
                mockLocation
        );

        var mockResponse = new TaskerResponse(
                UUID.randomUUID(),
                testAccountId,
                mockProfile
        );

        // when getByAccountId is called with testAccountId, return mockResponse
        when(taskerService.getByAccountId(testAccountId))
                .thenReturn(mockResponse);

        // 2. WHEN
        mockMvc.perform(
                get("/api/v1/tasker/user/me")
                        .header("Authorization", "Bearer " + validToken)
        )
                // 3. THEN
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(testAccountId.toString()))
                .andExpect(jsonPath("$.profile.about").value(mockProfile.about()))
                .andExpect(jsonPath("$.profile.location.address").value(mockLocation.address()));
    }
}