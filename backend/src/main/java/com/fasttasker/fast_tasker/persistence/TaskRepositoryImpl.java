package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
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
    public Optional<Task> findById(UUID id) {
        return jpa.findById(id);
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
