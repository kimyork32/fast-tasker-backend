package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 
 */
@Repository
public interface JpaTaskerRepository extends JpaRepository<Tasker, UUID>, ITaskerRepository {

    @Override
    Optional<Tasker> findByAccountId(UUID accountId);

}