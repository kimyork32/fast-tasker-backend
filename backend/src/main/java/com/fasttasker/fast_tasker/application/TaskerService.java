package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.application.exception.TaskerNotFoundException;
import com.fasttasker.fast_tasker.application.mapper.TaskerMapper;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
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
    private final IAccountRepository accountRepository;
    private final ITaskRepository taskRepository;
    private final TaskerMapper taskerMapper;


    public TaskerService(ITaskerRepository taskerRepository, IAccountRepository accountRepository, ITaskRepository taskRepository, TaskerMapper taskerMapper) {
        this.taskerRepository = taskerRepository;
        this.accountRepository = accountRepository;
        this.taskRepository = taskRepository;
        this.taskerMapper = taskerMapper;
    }

    /**
     * @param request tasker request from client
     */
    @Transactional
    public TaskerResponse registerTasker(TaskerRequest request) {
        UUID accountId = request.accountId();

        Tasker tasker = taskerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new TaskerNotFoundException(
                        "the tasker not found with account id: " + accountId));

        Profile profile = taskerMapper.toProfileEntity(request.profile());

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