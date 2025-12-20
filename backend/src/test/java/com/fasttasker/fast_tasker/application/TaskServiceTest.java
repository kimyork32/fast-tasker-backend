package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.task.*;
import com.fasttasker.fast_tasker.application.dto.tasker.ChatProfileResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.ProfileResponse;
import com.fasttasker.fast_tasker.application.mapper.TaskMapper;
import com.fasttasker.fast_tasker.application.mapper.TaskerMapper;
import com.fasttasker.fast_tasker.application.service.TaskService;
import com.fasttasker.fast_tasker.domain.task.Answer;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Question;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    @Mock
    private TaskerMapper taskerMapper;

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

    @Test
    void shouldAnswerQuestionSuccess() {
        // GIVEN
        UUID questionId = UUID.randomUUID();
        var answerRequest = AnswerRequest.builder()
                .questionId(questionId.toString())
                .description("answer description")
                .build();
        UUID taskId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID responderId = UUID.randomUUID();
        UUID answerId = UUID.randomUUID();

        // Mocks
        Task mockTask = mock(Task.class);
        Question mockQuestion = mock(Question.class);
        Answer mockAnswer = mock(Answer.class);
        Task mockSavedTask = mock(Task.class);
        Question mockSavedQuestion = mock(Question.class);
        Answer mockSavedAnswer = mock(Answer.class);
        Tasker mockTasker = mock(Tasker.class);
        ChatProfileResponse mockChatProfile = mock(ChatProfileResponse.class);
        AnswerResponse mockAnswerResponse = mock(AnswerResponse.class);
        AnswerProfileResponse mockProfileResponse = mock(AnswerProfileResponse.class);

        when(taskerRepository.findByAccountId(accountId)).thenReturn(mockTasker);
        when(mockTasker.getId()).thenReturn(responderId);
        when(taskRepository.findById(taskId)).thenReturn(mockTask);
        when(mockTask.getQuestionById(questionId)).thenReturn(mockQuestion);
        when(taskMapper.toAnswerEntity(answerRequest, responderId, mockQuestion)).thenReturn(mockAnswer);
        when(mockAnswer.getId()).thenReturn(answerId);
        when(mockSavedAnswer.getId()).thenReturn(answerId);
        when(taskRepository.save(mockTask)).thenReturn(mockSavedTask);
        when(mockSavedTask.getQuestionById(questionId)).thenReturn(mockSavedQuestion);
        when(mockSavedQuestion.getAnswers()).thenReturn(List.of(mockSavedAnswer));
        when(taskerRepository.findById(responderId)).thenReturn(mockTasker);
        when(taskerMapper.toChatProfileResponse(mockTasker)).thenReturn(mockChatProfile);
        when(taskMapper.toAnswerResponse(mockSavedAnswer)).thenReturn(mockAnswerResponse);
        when(taskMapper.toAnswerProfileResponse(mockAnswerResponse, mockChatProfile)).thenReturn(mockProfileResponse);

        // WHEN
        AnswerProfileResponse result = taskService.answerQuestion(answerRequest, taskId, accountId);

        // THEN
        assertThat(result)
                .isNotNull()
                .isEqualTo(mockProfileResponse);

        verify(mockQuestion).addAnswer(mockAnswer);
        verify(taskRepository).save(mockTask);

    }
}