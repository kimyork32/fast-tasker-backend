package com.fasttasker.fast_tasker.domain.tasker;

import java.util.List;
import java.util.UUID;

/**
 * 
 */
public interface ITaskerRepository {

    Tasker save(Tasker tasker);

    Tasker findById(UUID id);

    void deleteById(UUID id);

    List<Tasker> findAll();

    Tasker findByAccountId(UUID accountId);

    List<Tasker> findAllById(List<UUID> ids);
}