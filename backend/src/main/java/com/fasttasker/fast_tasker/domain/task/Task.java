package com.fasttasker.fast_tasker.domain.task;

import com.fasttasker.fast_tasker.domain.tasker.Location;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * AR for task.
 * This entity encapsulates all information about a posted job
 */
@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Task {

    @Id
    private UUID id;

    /**
     * Short and descriptive title for the task.
     * Limited to 100 characters.
     */
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    /**
     * Detailed description of the task requirements.
     * Limited to 500 characters.
     */
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    /**
     * The offered budget or amount for the task.
     * Note: Currently non-null, although future versions
     * might allow tasks without an initial budget.
     */
    @Column(name = "budget", nullable = false) // IMPROVEMENT: nullable may be nullable
    private int budget;

    /**
     * Physical location where the task must be performed.
     * Mapped as an Embedded Value Object.
     * The field names (latitude, longitude, address) are
     * mapped to specific columns in the 'task' table.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "latitude", column = @Column(name = "longitude", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "address", length = 255, nullable = false))
    })
    private Location location;

    /**
     * Specific date on which the task must be carried out.
     */
    @Column(name = "day", nullable = false)
    private LocalDate day;

    /**
     * Current status of the task lifecycle (e.g., OPEN, IN_PROGRESS).
     * Stored as String in the DB for better readability.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    /**
     * the ID of the account that posted this task.
     * ForeignKey that links it to the Account .
     */
    @Column(name = "poster_id", nullable = false)
    private UUID posterId;

    /**
     * List of questions posted by Taskers regarding this task.
     * This is the "One" side of the relationship. The 'Question'
     * entity manages the relationship (mappedBy = "task").
     * FetchType.LAZY: Questions are not loaded unless requested.
     * CascadeType.ALL: If this Task is deleted, its Questions are deleted too.
     */
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions;


    /**
     * List of offers made by Taskers for this task.
     * The 'Offer' entity manages the relationship (mappedBy = "task").
     * FetchType.LAZY: Offers are not loaded unless requested.
     * CascadeType.ALL: If this Task is deleted, its Offers are deleted too.
     */
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Offer> offers;

}