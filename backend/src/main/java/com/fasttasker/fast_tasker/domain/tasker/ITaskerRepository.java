package com.fasttasker.fast_tasker.domain.tasker;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 
 */
public interface ITaskerRepository {

    Tasker save(Tasker tasker);

    Optional<Tasker> findById(UUID id);

    void deleteById(UUID id);

    List<Tasker> findAll();

}