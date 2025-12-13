package com.fasttasker.fast_tasker.application.service;

import com.fasttasker.fast_tasker.application.dto.notification.NotificationResponse;
import com.fasttasker.fast_tasker.application.mapper.NotificationMapper;
import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.notification.Notification;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final INotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ITaskerRepository taskerRepository;

    public NotificationService(SimpMessagingTemplate messagingTemplate, INotificationRepository notificationRepository, NotificationMapper notificationMapper, ITaskerRepository taskerRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.taskerRepository = taskerRepository;
    }

    /**
     * @param receiverTaskerId tasker that receive notification
     * @param type type of the notification
     */
    public void sendNotification(UUID receiverTaskerId, UUID offerId, NotificationType type) {

        var notification = Notification.builder()
                .receiverTaskerId(receiverTaskerId)
                .targetId(offerId)
                .type(type)
                .build();

        // save notification
        Notification savedNotification = notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                receiverTaskerId.toString(),
                "/topic/notifications",
                notificationMapper.toNotificationResponse(savedNotification)
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

    public List<NotificationResponse> getAll(UUID accountId) {
        UUID taskerId = taskerRepository.findByAccountId(accountId).getId();
        List<Notification> notifications = notificationRepository.findAllByReceiverTaskerId(taskerId);

        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

}