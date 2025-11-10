package com.fasttasker.fast_tasker.domain.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 
 */
public interface ITaskRepository {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    void deleteById(UUID id);

    /**
     * find all tasks created by a specific user (Tasker)
     * @param posterId poster id (Tasker)
     * @return task list
     */
    List<Task> findByPosterId(UUID posterId);

    /**
     * find all tasks created by user that matches a specific status
     * @param posterId
     * @param status
     * @return task list
     */
    List<Task> findByPosterIdAndStatus(UUID posterId, TaskStatus status);
}