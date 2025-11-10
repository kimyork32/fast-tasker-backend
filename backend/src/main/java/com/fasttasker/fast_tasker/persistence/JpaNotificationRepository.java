package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * 
 */
@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, UUID> {

    // NOTHING Spring implemented all abstract methods (query methods conversions)

}