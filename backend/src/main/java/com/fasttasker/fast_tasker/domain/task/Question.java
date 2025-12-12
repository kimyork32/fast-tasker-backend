package com.fasttasker.fast_tasker.domain.task;

import com.fasttasker.fast_tasker.application.exception.DomainException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * it represents a question (Entity) asked about a specific task.
 * it is property of the Task AR
 */
@Entity
@Table(name = "question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class Question {

    @Id
    private UUID id;

    /**
     * content of the question
     * max size of 500 characters
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
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @ToString.Exclude
    private Task task;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Answer> answers = new ArrayList<>();

    @Builder(toBuilder = true)
    public Question(String description, UUID askedById, Task task) {
        validateDescription(description);
        if (askedById == null) {
            throw new IllegalArgumentException("AskedById cannot be null");
        }
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        this.id = UUID.randomUUID();
        this.description = description;
        this.askedById = askedById;
        this.task = task;
        this.status = QuestionStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public void editDescription(String newDescription) {
        validateDescription(newDescription);

        if (this.status == QuestionStatus.ANSWERED) {
            throw new DomainException("Cannot edit a question that has already been answered");
        }

        this.description = newDescription;
    }

    public void postAnswer(String answerContent, UUID responderId) {
        var newAnswer = Answer.builder()
                .description(answerContent)
                .responderId(responderId)
                .question(this)
                .build();

        this.answers.add(newAnswer);
        this.status = QuestionStatus.ANSWERED;
    }

    public void markAsDeleted() {
        this.status = QuestionStatus.DELETED;
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new DomainException("Question description cannot be empty");
        }
        if (description.length() > 500) {
            throw new DomainException("Question description exceeds 500 characters");
        }
    }
}