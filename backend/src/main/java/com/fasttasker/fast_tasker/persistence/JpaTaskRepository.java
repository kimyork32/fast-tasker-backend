package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * 
 */
@Repository
public interface JpaTaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByPosterId(UUID receiverTaskerId);

    List<Task> findByPosterIdAndStatus(UUID posterId, TaskStatus status);

    List<Task> findByStatus(TaskStatus status);

    // NOTE: Spring implemented the other methods (query methods conversions)

}