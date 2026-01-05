package com.fasttasker.notification.web.controller;

import com.fasttasker.notification.application.dto.NotificationRequest;
import com.fasttasker.notification.application.dto.NotificationResponse;
import com.fasttasker.notification.application.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request.getReceiverTaskerId(), request.getTargetId(), request.getType());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskerId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByTasker(@PathVariable UUID taskerId) {
        List<NotificationResponse> notifications = notificationService.getAll(taskerId);
        return ResponseEntity.ok(notifications);
    }
}