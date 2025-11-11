package com.fasttasker.fast_tasker.domain.tasker;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Profile {

    /**
     * 
     */
    @Column(name = "photo", length = 255, nullable = false)
    private String photo;

    /**
     * 
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "address", length = 255, nullable = false))
    })
    private Location location;

    /**
     * 
     */
    @Column(name = "about", length = 500, nullable = false)
    private String about;

    /**
     * 
     */
    @Column(name = "reputation", nullable = false)
    private int reputation;

    /**
     * 
     */
    @Column(name = "client_reviews", nullable = false)
    private int clientReviews;

    /**
     * 
     */
    @Column(name = "completed_tasks", nullable = false)
    private int completedTasks;
}