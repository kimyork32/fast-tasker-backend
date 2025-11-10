package com.fasttasker.fast_tasker.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AccountRepository used JPA repository.
 */
@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {
    // SOMETHING
}