package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.application.mapper.TaskMapper;
import com.fasttasker.fast_tasker.application.service.TaskService;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for TaskService
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldSaveTaskSuccess() {
        // 1. GIVEN
        var locationRequest = new LocationRequest(
                -13.412453,
                -12.158023,
                "address street",
                4141414
        );

        var taskRequest = new TaskRequest(
                "title for task",
                "description for task",
                200,
                locationRequest,
                LocalDate.parse("2025-11-14").toString()
        );

        // Mocking Mapper and Repository
        Task mockTask = mock(Task.class);
        TaskResponse mockResponse = mock(TaskResponse.class);

        when(taskMapper.toTaskEntity(any(TaskRequest.class))).thenReturn(mockTask);
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toResponse(any(Task.class))).thenReturn(mockResponse);
        when(mockResponse.title()).thenReturn(taskRequest.title());

        // 2. WHEN
        TaskResponse taskResponse = taskService.createTask(taskRequest, UUID.randomUUID());

        // 3. THEN
        // verify the DTO request
        assertThat(taskResponse).isNotNull();
        assertThat(taskResponse.title()).isEqualTo(taskRequest.title());
        verify(taskRepository).save(mockTask);
    }
}