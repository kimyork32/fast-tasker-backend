package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.notification.Notification;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final INotificationRepository notificationRepository;

    public NotificationService(SimpMessagingTemplate messagingTemplate, INotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    /**
     * @param receiverTaskerId tasker that receive notification
     * @param type type of the notification
     */
    public void sendNotification(UUID receiverTaskerId, NotificationType type) {
        var notification = new Notification(
                UUID.randomUUID(),
                receiverTaskerId,
                type
        );

        // save notification
        Notification savedNotification = notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                receiverTaskerId.toString(),
                "/topic/notifications",
                savedNotification
        );
    }

    /**
     * @param taskerId
     */
    public void getUnread(UUID taskerId) {
        // TODO implement here
    }

    /**
     * @param notificationId
     */
    public void markAsRead(UUID notificationId) {
        // TODO implement here
    }

    /**
     * @param taskerId
     */
    public void getAll(UUID taskerId) {
        // TODO implement here
    }

}