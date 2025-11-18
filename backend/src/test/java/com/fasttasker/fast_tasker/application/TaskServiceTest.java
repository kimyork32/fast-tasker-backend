package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * integration test for TaskService
 */
@SpringBootTest
@Transactional
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ITaskRepository taskRepository;

    @Test
    void shouldSaveTaskSuccess() {
        // 1. GIVEN
        var locationRequest = new LocationRequest(
                -13.412453,
                -12.158023,
                "address street"
        );

        var taskRequest = new TaskRequest(
                "title for task",
                "description for task",
                200,
                locationRequest,
                LocalDate.parse("2025-11-14").toString()
        );

        // 2. WHEN
        TaskResponse taskResponse = taskService.createTask(taskRequest, UUID.randomUUID());

        // 3. THEN
        // verify the DTO request
        assertThat(taskResponse).isNotNull();
        assertThat(taskResponse.title()).isEqualTo(taskRequest.title());

    }
}