package com.fasttasker.fast_tasker.application;

import domain.notification.INotificationRepository;
import domain.notification.NotificationType;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class NotificationService {

    /**
     * Default constructor
     */
    public NotificationService() {
    }

    /**
     * 
     */
    private INotificationRepository notificationRepository;



    /**
     * @param receiverTaskerId 
     * @param type 
     * @param message
     */
    public void sendNotification(UUID receiverTaskerId, NotificationType type, String message) {
        // TODO implement here
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