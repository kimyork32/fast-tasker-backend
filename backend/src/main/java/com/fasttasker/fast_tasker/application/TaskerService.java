package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.conversation.ConversationRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageContentRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageRequest;
import com.fasttasker.fast_tasker.application.dto.task.AssignTaskerRequest;
import com.fasttasker.fast_tasker.application.dto.task.AssignTaskerResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.application.exception.TaskAccessDeniedException;
import com.fasttasker.fast_tasker.application.exception.TaskerNotFoundException;
import com.fasttasker.fast_tasker.application.mapper.TaskerMapper;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskerService {

    private final ITaskerRepository taskerRepository;
    private final ITaskRepository taskRepository;
    private final TaskerMapper taskerMapper;
    private final NotificationService notificationService;
    private final ConversationService conversationService;

    public TaskerService(ITaskerRepository taskerRepository, ITaskRepository taskRepository, TaskerMapper taskerMapper,
                         NotificationService notificationService, ConversationService conversationService) {
        this.taskerRepository = taskerRepository;
        this.notificationService = notificationService;
        this.taskRepository = taskRepository;
        this.taskerMapper = taskerMapper;
        this.conversationService = conversationService;
    }

    /**
     * @param request tasker request from client
     */
    @Transactional
    public TaskerResponse registerTasker(TaskerRequest request) {
        UUID accountId = UUID.fromString(request.accountId());
        Tasker tasker = taskerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new TaskerNotFoundException(
                        "the tasker not found with account id: " + accountId));

        Profile profile = taskerMapper.toProfileEntity(request.profile());

        tasker.updateProfile(profile);

        taskerRepository.save(tasker);

        return taskerMapper.toResponse(tasker);
    }

    /**
     * @param taskerId tasker id
     */
    @Transactional(readOnly = true)
    public TaskerResponse getById(UUID taskerId) {
        Optional<Tasker> taskerOpt = taskerRepository.findById(taskerId);

        if (taskerOpt.isEmpty()) {
            throw new TaskerNotFoundException(
                    "the tasker not found with id: " + taskerId);
        }

        Tasker tasker = taskerOpt.get();

        return taskerMapper.toResponse(tasker);
    }

    /**
     * get TaskerResponse by account id
     * 
     * @param taskerId tasker id
     * @return taskerResponse
     */
    @Transactional(readOnly = true)
    public TaskerResponse getByAccountId(UUID taskerId) {
        Tasker tasker = taskerRepository.findById(taskerId)
                .orElseThrow(() -> new TaskerNotFoundException("TaskerService. getByAccountId. tasker not fount"));
        return taskerMapper.toResponse(tasker);
    }

    @Transactional
    public AssignTaskerResponse assignTaskToTasker(AssignTaskerRequest request, UUID posterId) {
        UUID taskId = UUID.fromString(request.taskId());
        UUID taskerId = UUID.fromString(request.taskerId());
        UUID offerId = UUID.fromString(request.offerId());

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskerNotFoundException("Task not found with id: " + taskId));

        if (!posterId.equals(task.getPosterId())) {
            throw new TaskAccessDeniedException("User does not have permission to assign this task.");
        }

        // save tasker how assignTasker in the task
        task.setAssignedTaskerId(taskerId);
        taskRepository.save(task);

        // notifying of the tasker that the task has been assigned
        notificationService.sendNotification(taskerId, offerId, NotificationType.OFFER_ACCEPTED);
        var conversationRequest = new ConversationRequest(
                taskId,
                posterId,
                taskerId
        );

        // create a new conversation between the poster and the tasker
        UUID conversationId = conversationService.startConversation(conversationRequest);

        // poster sends a message to the tasker
        var messageRequest = new MessageRequest(
                conversationId,
                new MessageContentRequest("Hola, te he asignado una tarea :)", null)
        );
        conversationService.processAndSendMessage(messageRequest, posterId);

        return taskerMapper.toAssignTaskerResponse(request);
    }

    /**
     * @param taskerId
     */
    public void getTaskHistory(UUID taskerId) {
        // TODO implement here
    }

    /**
     * @param taskerId
     */
    public void getActiveTasks(UUID taskerId) {
        // TODO implement here
    }

}