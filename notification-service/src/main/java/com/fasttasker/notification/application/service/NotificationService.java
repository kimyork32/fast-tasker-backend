package com.fasttasker.notification.application.service;

import com.fasttasker.notification.application.dto.NotificationRequest;
import com.fasttasker.notification.application.dto.NotificationResponse;
import com.fasttasker.notification.application.mapper.NotificationMapper;
import com.fasttasker.notification.config.RabbitMQConfig;
import com.fasttasker.notification.domain.INotificationRepository;
import com.fasttasker.notification.domain.Notification;
import com.fasttasker.notification.domain.NotificationType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final INotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(SimpMessagingTemplate messagingTemplate, INotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleNotification(NotificationRequest request) {
        sendNotification(request.getReceiverTaskerId(), request.getTargetId(), request.getType());
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

    public List<NotificationResponse> getAll(UUID taskerId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverTaskerId(taskerId);

        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

}