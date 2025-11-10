package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
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
public interface JpaTaskRepository extends JpaRepository<Task, UUID>, ITaskRepository {

    List<Task> findByReceiverTaskerId(UUID receiverTaskerId);

    List<Task> findByReceiverTaskerIdAndStatus(UUID receiverTaskerId, TaskStatus status);

    // NOTE: Spring implemented the other methods (query methods conversions)

}