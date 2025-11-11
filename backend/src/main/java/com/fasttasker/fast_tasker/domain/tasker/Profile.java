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