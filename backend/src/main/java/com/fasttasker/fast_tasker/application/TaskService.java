package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.application.mapper.TaskMapper;
import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 
 */
@Service
public class TaskService {

    private final ITaskRepository taskRepository;
    private final ITaskerRepository taskerRepository;
    private final INotificationRepository notificationRepository;
    private final TaskMapper taskMapper;

    public TaskService(
            ITaskRepository taskRepository,
            ITaskerRepository taskerRepository,
            INotificationRepository notificationRepository, TaskMapper taskMapper
    ) {
        this.taskRepository = taskRepository;
        this.taskerRepository = taskerRepository;
        this.notificationRepository = notificationRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * 
     */
    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest, UUID posterId) {
        Task newTask = taskMapper.toEntity(taskRequest);

        // assign the values here, ignoring what comes from the client
        newTask.setPosterId(posterId);
        newTask.setStatus(TaskStatus.ACTIVE); // assign the default initial state

        taskRepository.save(newTask);

        return taskMapper.toResponse(newTask);
    }

    /**
     * return all active tasks
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> listActiveTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 
     */
    public void editTask() {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void publishTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param taskedId 
     * @param price 
     * @param description
     */
    public void createOffer(UUID taskId, UUID taskedId, int price, String description) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param offerId
     */
    public void acceptOffer(UUID taskId, UUID offerId) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param askerId 
     * @param question
     */
    public void askQuestion(UUID taskId, UUID askerId, String question) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param questionId 
     * @param answer
     */
    public void answerQuestion(UUID taskId, UUID questionId, String answer) {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void completeTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void cancelTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * @param city
     */
    public void listByLocation(String city) {
        // TODO implement here
    }

    /**
     * @param status
     */
    public void listByStatus(TaskStatus status) {
        // TODO implement here
    }

    public TaskMapper getTaskMapper() {
        return taskMapper;
    }
}