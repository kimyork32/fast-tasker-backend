package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.application.mapper.TaskMapper;
import com.fasttasker.fast_tasker.application.service.TaskService;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
    private ITaskerRepository taskerRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldSaveTaskSuccess() {
        // 1. GIVEN
        var locationRequest = LocationRequest.builder()
                .latitude(-13.412453)
                .longitude(-12.158023)
                .address("address street")
                .zip("04014")
                .build();

        var taskRequest = TaskRequest.builder()
                .title("title for task")
                .description("description for task")
                .budget(200)
                .location(locationRequest)
                .taskDate("2030-12-31")
                .build();

        // Mocking Mapper and Repository
        Tasker mockTasker = mock(Tasker.class);
        Task mockTask = mock(Task.class);
        Task mockSavedTask = mock(Task.class);
        UUID mockAccountId = UUID.randomUUID();
        UUID mockPosterId = UUID.randomUUID();
        TaskResponse mockResponse = mock(TaskResponse.class);

        when(taskerRepository.findByAccountId(mockAccountId)).thenReturn(mockTasker);
        when(mockTasker.getId()).thenReturn(mockPosterId);
        when(taskMapper.toTaskEntity(taskRequest, mockPosterId)).thenReturn(mockTask);
        when(taskRepository.save(mockTask)).thenReturn(mockSavedTask);
        when(taskMapper.toResponse(mockSavedTask)).thenReturn(mockResponse);
        when(mockResponse.title()).thenReturn(taskRequest.title());

        // 2. WHEN
        TaskResponse taskResponse = taskService.createTask(taskRequest, mockAccountId);

        // 3. THEN
        // verify the DTO response
        assertThat(taskResponse).isNotNull();
        assertThat(taskResponse.title()).isEqualTo(taskRequest.title());
        verify(taskRepository).save(mockTask);
    }
}