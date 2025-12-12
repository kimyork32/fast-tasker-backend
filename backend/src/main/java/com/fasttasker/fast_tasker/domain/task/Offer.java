package com.fasttasker.fast_tasker.domain.task;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * 
 */
@Entity
@Table(name = "offer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Offer {

    /**
     * 
     */
    @Id
    private UUID id;

    /**
     * budget must be a positive integer
     * greater than 5 and less than 999
     */
    @Column(name = "price")
    private int price;

    /**
     * max size of 500 characters
     */
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    /**
     * 
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OfferStatus status;

    /**
     * 
     */
    @Column(name = "offerted_by_id", nullable = false)
    private UUID offertedById;

    /**
     * 
     */
    @Column(name = "create_at", nullable = false)
    private Instant createdAt;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @ToString.Exclude
    private Task task;

}