package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.task.OfferRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.OfferResponse;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.exception.AccountNotFoundException;
import com.fasttasker.fast_tasker.application.exception.TaskNotFoundException;
import com.fasttasker.fast_tasker.application.mapper.TaskMapper;
import com.fasttasker.fast_tasker.domain.account.Account;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.task.*;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    private final IAccountRepository accountRepository;
    private final TaskMapper taskMapper;

    public TaskService(
            ITaskRepository taskRepository,
            ITaskerRepository taskerRepository,
            INotificationRepository notificationRepository, IAccountRepository accountRepository, TaskMapper taskMapper
    ) {
        this.taskRepository = taskRepository;
        this.taskerRepository = taskerRepository;
        this.notificationRepository = notificationRepository;
        this.accountRepository = accountRepository;
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
     * return all active (status) tasks
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> listActiveTasks() {
        return taskRepository.findByStatus(TaskStatus.ACTIVE)
                .stream() // convert to stream
                .map(taskMapper::toResponse) // It applies a function (toResponse) to each element of
                                             // the stream and returns a new stream with the results (TaskResponse)
                .collect(Collectors.toList());  // after processing the stream elements, it reconstructs
                                                // the result into a concrete collection (List)
    }

    /**
     * return all tasks created by a specific user (Tasker)
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> listTasksByPoster(UUID posterId) {
        return taskRepository.findByPosterId(posterId)
                .stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("getTask exception: invalid task ID"));
        return taskMapper.toResponse(task);
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
     *
     * @param offerRequest request of the offer
     * @param taskId id of the task
     * @param accountId id of the tasker, find the taskId with this
     */
    @Transactional
    public OfferResponse createOffer(OfferRequest offerRequest, UUID taskId, UUID accountId) {
        // find taskerId with the account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        UUID taskerId = account.getTaskerId();

        // find task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        Offer offer = taskMapper.toOfferEntity(offerRequest);
        // insert values of the offer
        offer.setStatus(OfferStatus.PENDING);
        offer.setOffertedById(taskerId); // Corrected: Should be the ID of the user making the offer
        offer.setCreatedAt(Instant.now());
        offer.setTask(task);

        // add the offer to the task
        task.getOffers().add(offer);

        Task savedTask = taskRepository.save(task);

        // Find the newly added offer from the saved task entity to ensure we return the persisted state.
        Offer savedOffer = savedTask.getOffers().stream()
                .filter(o -> o.getId().equals(offer.getId())).findFirst().orElse(offer);
        return taskMapper.toOfferResponse(savedOffer);
    }

    /**
     * get the list of the offer by taskId
     * @param taskId task id
     * @return list of offer
     */
    @Transactional(readOnly = true)
    public List<OfferResponse> listOffersByTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        // cool
        return task.getOffers().stream()
                .map(taskMapper::toOfferResponse)
                .collect(Collectors.toList());
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