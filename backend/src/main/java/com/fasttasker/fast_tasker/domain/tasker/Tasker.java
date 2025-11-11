package com.fasttasker.fast_tasker.domain.tasker;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * 
 */
@Entity
@Table(name = "tasker")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Tasker {

    /**
     * 
     */
    @Id
    private UUID id;

    /**
     * 
     */
    @Column(name = "account_id", unique = true, nullable = false)
    private UUID accountId;

    /**
     * 
     */
    @Embedded
    @AttributeOverrides({
            // Mapeo de campos directos de 'Profile'
            @AttributeOverride(name = "photo", column = @Column(name = "profile_photo_url", length = 255)),
            @AttributeOverride(name = "about", column = @Column(name = "profile_about", length = 500)),
            @AttributeOverride(name = "reputation", column = @Column(name = "profile_reputation")),
            @AttributeOverride(name = "clientReviews", column = @Column(name = "profile_client_reviews")),
            @AttributeOverride(name = "completedTasks", column = @Column(name = "profile_completed_tasks")),

            // Mapeo de campos anidados de 'Profile.location'
            // Se usa la sintaxis "dot notation" (punto).
            @AttributeOverride(name = "location.latitude", column = @Column(name = "location_latitude", nullable = false)),
            @AttributeOverride(name = "location.longitude", column = @Column(name = "location_longitude", nullable = false)),
            @AttributeOverride(name = "location.address", column = @Column(name = "location_address", length = 255, nullable = false))
    })
    private Profile profile;

    // private List<Task> tasks; DELETED: i removed it because it breaks a fundamental rule of DDD called the Aggregates Rule

    /**
     * 
     */
    public void addTask() {
        // TODO implement here
    }

    /**
     * 
     */
    public void getActiveTasks() {
        // TODO implement here
    }

    /**
     * 
     */
    public void getHistory() {
        // TODO implement here
    }

}