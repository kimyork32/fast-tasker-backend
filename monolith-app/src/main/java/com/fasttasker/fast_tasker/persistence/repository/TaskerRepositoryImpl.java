package com.fasttasker.fast_tasker.persistence.repository;

import com.fasttasker.fast_tasker.application.exception.TaskerNotFoundException;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import com.fasttasker.fast_tasker.persistence.jpa.JpaTaskerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TaskerRepositoryImpl implements ITaskerRepository {

    private final JpaTaskerRepository jpa;

    public TaskerRepositoryImpl(JpaTaskerRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Tasker save(Tasker tasker) {
        return jpa.save(tasker);
    }

    @Override
    public Tasker findById(UUID id) {
        return jpa.findById(id)
                .orElseThrow(() -> new TaskerNotFoundException(id));
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Tasker> findAll() {
        return jpa.findAll();
    }

    @Override
    public Tasker findByAccountId(UUID accountId) {
        return jpa.findByAccountId(accountId)
                .orElseThrow(() -> TaskerNotFoundException.fromAccountId(accountId));
    }

    @Override
    public List<Tasker> findAllById(List<UUID> ids) {
        return jpa.findAllById(ids);
    }
}