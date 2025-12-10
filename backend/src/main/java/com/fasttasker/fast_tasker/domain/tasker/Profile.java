package com.fasttasker.fast_tasker.domain.tasker;

import jakarta.persistence.*;
import lombok.*;

/**
 * 
 */
@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Profile {
    /**
     * minimum one name required and maximum three (words)
     */
    private String firstName;

    /**
     * minimum one name required and maximum three (words)
     */
    private String lastName;

    /**
     * url of the photo
     */
    private String photo;

    private Location location;

    /**
     * max 200 characters
     */
    private String about;

    /**
     * average of the stars given by the clients
     * each star is a value between 0 and 5.
     * one decimal place precision
     */
    private float reputation;

    /**
     * number of the comments given by the clients
     */
    private int clientReviews;

    private int completedTasks;


    @Builder(toBuilder = true)
    public Profile(String firstName, String lastName, String photo, Location location, String about, float reputation,
                   int clientReviews, int completedTasks) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("firstName and lastName cannot be null");
        }
        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("firstName and lastName cannot be empty");
        }
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        }
        if (about.length() > 200) {
            throw new IllegalArgumentException("about cannot be longer than 200 characters");
        }
        if (reputation < 0 || reputation > 5) {
            throw new IllegalArgumentException("reputation must be between 0 and 5");

        }
        if (clientReviews < 0) {
            throw new IllegalArgumentException("clientReviews cannot be negative");
        }
        if (completedTasks < 0) {
            throw new IllegalArgumentException("completedTasks cannot be negative");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.location = location;
        this.about = about;
        this.reputation = reputation;
        this.clientReviews = clientReviews;
        this.completedTasks = completedTasks;
    }
}