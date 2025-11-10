package com.fasttasker.fast_tasker.domain.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * represent the root of AR for notification.
 */
@Entity
@Table(name = "notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Notification {

    /**
     * unique ID generates for the application. Primary Key (PK)
     */
    @Id
    private UUID  id;

    /**
     * ID of the tasker that receives this notification
     */
    @Column(name = "receiver_tasker_id", nullable = false)
    private UUID receiverTaskerId;


    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;


    private String message;

    /**
     * date and time the notification was created
     */
    private LocalDateTime createdAt;

    /**
     * boolean indicator of whether the notification was read
     */
    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    /**
     * notification status:
     * {READ, UNREAD}
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

}