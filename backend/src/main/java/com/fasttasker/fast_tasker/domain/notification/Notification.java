package com.fasttasker.fast_tasker.domain.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * represent the root of AR for notification.
 */
@Entity
@Table(name = "notification")
@Getter
@Setter // Consider making entities immutable by removing setters where possible
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
    private Instant createdAt;

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

    @Builder
    public Notification(UUID id, UUID receiverTaskerId, NotificationType type) {
        this.id = id;
        this.receiverTaskerId = receiverTaskerId;
        this.type = type;
        this.status = NotificationStatus.UNREAD;
        this.createdAt = Instant.now();
        this.isRead = false;

        this.createMessageContent();
    }

    /**
     * this method sets the message content based on the notification type.
     * it is called during object construction to ensure consistency.
     */
    public void createMessageContent() {
        this.message = switch (type) {
            case NotificationType.QUESTION -> "Tienes una nueva pregunta";
            case NotificationType.OFFER_ACCEPTED -> "Has aceptado una oferta";
            case NotificationType.SYSTEM -> "Notificación del sistema";
            case NotificationType.TASK_COMPLETED -> "Has completado una tarea :)";
            case NotificationType.NEW_MESSAGE -> "Tienes un nuevo mensaje";
        };
    }
}