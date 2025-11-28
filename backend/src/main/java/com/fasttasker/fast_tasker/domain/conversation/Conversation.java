package com.fasttasker.fast_tasker.domain.conversation;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * one-to-one conversation (poster to tasker)
 */
@Entity
@Table(name = "conversation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter // this no shouldn't be here before it breaks DDD
@ToString(exclude = "messages") // avoid infinite loop
public class Conversation {

    // NOTE: add semantic constructor (DDD) for validate attributes, and add ID generation (annotation with
    // strategy = GenerationType.UUID)

    @Id
    private UUID id;

    /**
     * task is the context for the conversation
     */
    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "participant_a_id", nullable = false)
    private UUID participantA;

    @Column(name = "participant_b_id", nullable = false)
    private UUID participantB;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @Enumerated(EnumType.STRING)
    private ConversationStatus status;
}