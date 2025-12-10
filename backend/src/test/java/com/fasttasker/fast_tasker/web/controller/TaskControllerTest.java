package com.fasttasker.fast_tasker.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasttasker.fast_tasker.application.TaskService;
import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationResponse;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * integration test
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void shouldCreateNewTaskSuccessfully() throws Exception {
        // 1. GIVEN
        var posterId = UUID.randomUUID();
        var taskId = UUID.randomUUID();
        var locationReq = new LocationRequest(
                -13.412453,
                -12.158023,
                "address request",
                414144
        );

        var taskRequest = new TaskRequest(
                "title task",
                "description task",
                100,
                locationReq,
                "2025-11-17T10:00:00Z"
        );

        var locationResp = LocationResponse.builder()
                .latitude(locationReq.latitude())
                .longitude(locationReq.longitude())
                .address(locationReq.address())
                .build();

        var taskResponse = new TaskResponse(
                taskId.toString(),
                "title task",
                "description task",
                100,
                locationResp,
                "2025-11-17T10:00:00Z",
                TaskStatus.ACTIVE.name(),
                posterId.toString()
        );

        // when create task (service) with any TaskRequest and posterId, then return the taskResponse
        when(taskService.createTask(any(TaskRequest.class), eq(posterId)))
                .thenReturn(taskResponse);

        // simulating an authenticated user
        var authentication = new UsernamePasswordAuthenticationToken(posterId, null, null);

        // 2. WHEN & THEN
        mockMvc.perform(
                post("/api/v1/tasks")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("title task"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.posterId").value(posterId.toString()));
    }
}