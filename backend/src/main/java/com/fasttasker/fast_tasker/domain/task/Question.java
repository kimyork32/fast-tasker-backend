package com.fasttasker.fast_tasker.domain.task;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * it represents a question (Entity) asked about a specific task.
 * it is property of the Task AR
 */
@Entity
@Table(name = "question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question {

    @Id
    private UUID id;

    /**
     * content of the question
     * size of 500 characters
     */
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    /**
     * status of the question
     * { PENDING, ANSWERED, DELETED }
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuestionStatus status;

    /**
     * the id of the tasker that asked the question
     */
    @Column(name = "asked_by_id", nullable = false)
    private UUID askedById;

    /**
     * date and time the question was created
     */
    @Column(name = "create", nullable = false)
    private LocalDateTime createdAt;


    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @ToString.Exclude
    private Task task;

}