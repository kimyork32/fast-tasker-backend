package com.fasttasker.fast_tasker.domain.task;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
/**
 * answer of the question
 */
@Entity
@Table(name = "answer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Answer {

    @Id
    private UUID id;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "answered_id", nullable = false)
    private UUID answeredId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @ToString.Exclude
    private Question question;
}
