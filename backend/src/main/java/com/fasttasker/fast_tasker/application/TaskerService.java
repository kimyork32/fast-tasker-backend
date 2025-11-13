package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.TaskerResponse;
import com.fasttasker.fast_tasker.application.exception.TaskerNotFoundException;
import com.fasttasker.fast_tasker.application.mapper.TaskerMapper;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Service
public class TaskerService {

    private final ITaskerRepository taskerRepository;
    private final IAccountRepository accountRepository;
    private final ITaskRepository taskRepository;
    private final TaskerMapper taskerMapper;


    public TaskerService(ITaskerRepository taskerRepository, IAccountRepository accountRepository, ITaskRepository taskRepository, ITaskerFactory taskerFactory, TransactionalOperator transactional, TaskerMapper taskerMapper) {
        this.taskerRepository = taskerRepository;
        this.accountRepository = accountRepository;
        this.taskRepository = taskRepository;
        this.taskerMapper = taskerMapper;
    }

    /**
     * @param accountId account id
     * @param profile profile of the tasker
     */
    @Transactional
    public TaskerResponse registerTasker(UUID accountId, Profile profile) {
        Optional<Tasker> taskerOpt = taskerRepository.findByAccountId(accountId);

        if (taskerOpt.isEmpty()) {
            throw new TaskerNotFoundException(
                    "the tasker not found with account id: " + accountId);
        }

        Tasker tasker = taskerOpt.get();
        tasker.setProfile(profile);

        taskerRepository.save(tasker);
        return taskerMapper.toResponse(tasker);
    }

    /**
     * @param taskerId tasker id
     */
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
     * @param taskerId 
     * @param taskId
     */
    public void assignTaskToTasker(UUID taskerId, UUID taskId) {
        // TODO implement here
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