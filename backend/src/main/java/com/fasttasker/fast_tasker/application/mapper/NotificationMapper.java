package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.notification.NotificationResponse;
import com.fasttasker.fast_tasker.domain.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    // to Response ///////////////////////////
    public NotificationResponse toNotificationResponse(Notification notification) {
        if (notification == null) return null;

        return NotificationResponse.builder()
                .id(notification.getId().toString())
                .receiverTaskerId(notification.getReceiverTaskerId().toString())
                .type(notification.getType().name())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt().toString())
                .isRead(notification.isRead())
                .status(notification.getStatus().name())
                .build();
    }
}
