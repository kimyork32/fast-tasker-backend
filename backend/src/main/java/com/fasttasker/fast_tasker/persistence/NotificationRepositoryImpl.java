package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.notification.Notification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationRepositoryImpl implements INotificationRepository {

    JpaNotificationRepository jpa;

    public NotificationRepositoryImpl(JpaNotificationRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Notification save(Notification notification) {
        return jpa.save(notification);
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public List<Notification> findAll() {
        return jpa.findAll();
    }

    @Override
    public Optional<Notification> findByReceiverTaskerId(UUID receiverTaskerId) {
        return jpa.findByReceiverTaskerId(receiverTaskerId);
    }
}
