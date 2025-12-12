package com.fasttasker.fast_tasker.persistence.repository;

import com.fasttasker.fast_tasker.application.exception.TaskNotFoundException;
import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import com.fasttasker.fast_tasker.persistence.jpa.JpaTaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryImpl implements ITaskRepository {

    private final JpaTaskRepository jpa;

    public TaskRepositoryImpl(JpaTaskRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Task save(Task task) {
        return jpa.save(task);
    }

    @Override
    public List<Task> saveAll(List<Task> tasks) {
        return jpa.saveAll(tasks);
    }

    @Override
    public List<Task> findAll() {
        return jpa.findAll();
    }

    @Override
    public Task findById(UUID id) {
        return jpa.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return jpa.findByStatus(status);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Task> findByPosterId(UUID posterId) {
        return jpa.findByPosterId(posterId);
    }

    @Override
    public List<Task> findByPosterIdAndStatus(UUID posterId, TaskStatus status) {
        return jpa.findByPosterIdAndStatus(posterId, status);
    }
}
