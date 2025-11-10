package com.fasttasker.fast_tasker.domain.notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface INotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID id);

    List<Notification> findAll();

    Optional<Notification> findByReceiverTaskerId(UUID receiverTaskerId);

}