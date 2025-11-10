package com.fasttasker.fast_tasker.domain.notification;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 
 */
public class Notification {

    /**
     * Default constructor
     */
    public Notification() {
    }

    /**
     * 
     */
    private UUID  id;

    /**
     * 
     */
    private UUID receiverTaskerId;

    /**
     * 
     */
    private NotificationType type;

    /**
     * 
     */
    private String message;

    /**
     * 
     */
    private LocalDateTime createdAt;

    /**
     * 
     */
    private boolean isRead;

    /**
     * 
     */
    private NotificationStatus status;



}