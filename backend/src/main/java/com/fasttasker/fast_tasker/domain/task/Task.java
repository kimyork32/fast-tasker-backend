package com.fasttasker.fast_tasker.domain.task;

import com.fasttasker.fast_tasker.application.exception.DomainException;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AR for task.
 * This entity encapsulates all information about a posted job
 */
@Entity
@Table(name = "task")
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(exclude = {"questions", "offers"})
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
     * might allow tasks without an initial budget.
     * budget must be a positive integer a maximum 999
     */
    @Column(name = "budget", nullable = false) // IMPROVEMENT: nullable may be nullable
    private int budget;

    /**
     * Physical location where the task must be performed.
     * Mapped as an Embedded Value Object.
     */
    @Embedded
    private Location location;

    /**
     * Specific date on which the task must be carried out.
     */
    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

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
     * the ID of the tasker assigned to complete this task.
     * This is null when the task is created (OPEN).
     * it gets populated when and Offer is accepted
     */
    @Column(name = "assigned_tasker_id")
    private UUID assignedTaskerId;

    /**
     * List of questions posted by Taskers regarding this task.
     * This is the "One" side of the relationship. The 'Question'
     * entity manages the relationship (mappedBy = "task").
     * FetchType.LAZY: Questions are not loaded unless requested.
     * CascadeType.ALL: If this Task is deleted, its Questions are deleted too.
     */
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Question> questions = new ArrayList<>();

    /**
     * List of offers made by Taskers for this task.
     * The 'Offer' entity manages the relationship (mappedBy = "task").
     * FetchType.LAZY: Offers are not loaded unless requested.
     * CascadeType.ALL: If this Task is deleted, its Offers are deleted too.
     */
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Offer> offers = new ArrayList<>();

    @Builder(toBuilder = true)
    public Task(String title, String description, int budget, Location location, LocalDate taskDate) {
        if (title == null || title.isEmpty()) {
            throw new DomainException("Title cannot be null or empty");
        }
        if (description == null || description.isEmpty()) {
            throw new DomainException("Description cannot be null or empty");
        }
        if (budget < 0 || budget > 999) {
            throw new DomainException("Budget must be between 0 and 999");
        }
        if (location == null) {
            throw new DomainException("Location cannot be null");
        }
        if (taskDate == null) {
            throw new DomainException("Task date cannot be null");
        }
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.location = location;
        this.taskDate = taskDate;
        this.status = TaskStatus.ACTIVE;
    }

    public void updateDetails(String newTitle, String newDescription) {
        if (newTitle == null || newTitle.isEmpty()) {
            throw new DomainException("Title cannot be null or empty");
        }
        if (newDescription == null || newDescription.isEmpty()) {
            throw new DomainException("Description cannot be null or empty");
        }
        if (this.status == TaskStatus.COMPLETED) {
            throw new DomainException("Cannot update details of a completed task");
        }

        this.title = newTitle;
        this.description = newDescription;
    }

    public void adjustBudget(int newBudget) {
        if (budget < 0 || budget > 999) {
            throw new DomainException("Budget must be between 0 and 999");
        }
        if (!this.offers.isEmpty() && newBudget < this.budget) {
            throw new DomainException("Cannot decrease budget when offers exist");
        }

        this.budget = newBudget;
    }

    public void assignTasker(UUID taskerId, int agreedPrice) {
        if (taskerId == null) {
            throw new DomainException("Tasker ID cannot be null");
        }
        if (this.status != TaskStatus.ACTIVE) {
            throw new DomainException("Only active tasks can be assigned");
        }

        this.budget = agreedPrice;
        this.assignedTaskerId = taskerId;
        this.status = TaskStatus.ASSIGNED;
    }

    // ASSIGNED to IN_PROGRESS
    public void startWork() {
        if (this.status != TaskStatus.ASSIGNED) {
            throw new DomainException("The task must be assigned in order to begin");
        }
        this.status = TaskStatus.IN_PROGRESS;
    }

    /**
     * ASSIGNED/IN_PROGRESS to COMPLETED
     */
    public void completeTask() {
        if (this.status != TaskStatus.ASSIGNED && this.status != TaskStatus.IN_PROGRESS) {
            throw new DomainException("You cannot complete a task that is not in progress or assigned");
        }
        this.status = TaskStatus.COMPLETED;
    }

    // cancel. This is complex
    public void cancel() {
        if (this.status == TaskStatus.COMPLETED) {
            throw new DomainException("You cannot cancel a task that has already been completed");
        }
        // if it's ASSIGNED, there would be a refund logic here
        this.status = TaskStatus.CANCELLED;
    }
}