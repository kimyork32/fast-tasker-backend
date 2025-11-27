package com.fasttasker.fast_tasker.domain.tasker;

import jakarta.persistence.*;
import lombok.*;

/**
 * 
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Profile {
    /**
     *
     */
    private String firstName;

    /**
     *
     */
    private String lastName;

    /**
     * 
     */
    private String photo;

    /**
     * 
     */
    private Location location;

    /**
     * 
     */
    private String about;

    /**
     * 
     */
    private int reputation;

    /**
     * 
     */
    private int clientReviews;

    /**
     * 
     */
    private int completedTasks;
}