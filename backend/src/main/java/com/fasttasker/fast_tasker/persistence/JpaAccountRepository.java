package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.account.Account;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * implementation of the IAccountrepository using Spring Data JPA.
 */
@Repository
public interface JpaAccountRepository extends JpaRepository<Account, UUID>, IAccountRepository {
    // NOTHING Spring implemented 'save' and 'findById' and 'findByEmailValue' (query methods conversions).
}