package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 
 */
@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, UUID>, INotificationRepository {

    Optional<Notification> findByReceiverTaskerId(UUID receiverTaskerId);

    // NOTE: Spring implemented the other methods (query methods conversions)

}