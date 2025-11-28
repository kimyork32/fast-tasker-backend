package com.fasttasker.fast_tasker.domain.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    private UUID id;

    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "text", column = @Column(name = "text", nullable = false)),
            @AttributeOverride(name = "attachmentUrl", column = @Column(name = "attachmentUrl", nullable = false))
    })
    private MessageContent content;

    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    /**
     * moment in that is read
     */
    @Column(name = "read_at")
    private Instant readAt;

    /**
     * mark as read in this time
     */
    public void markAsRead() {
        if (readAt != null) {
            readAt = Instant.now();
        }
    }

    public boolean isRead() {
        return readAt != null;
    }
}
