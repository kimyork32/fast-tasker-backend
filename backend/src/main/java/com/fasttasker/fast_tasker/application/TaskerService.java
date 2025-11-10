package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Profile;

import java.util.UUID;

/**
 * 
 */
public class TaskerService {

    /**
     * Default constructor
     */
    public TaskerService() {
    }

    /**
     * 
     */
    private ITaskerRepository taskerRepository;

    /**
     * 
     */
    private IAccountRepository accountRepository;

    /**
     * 
     */
    private ITaskRepository taskRepository;




    /**
     * @param accountId 
     * @param profile
     */
    public void registerTasker(UUID accountId, Profile profile) {
        // TODO implement here
    }

    /**
     * @param taskerId
     */
    public void getById(UUID taskerId) {
        // TODO implement here
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