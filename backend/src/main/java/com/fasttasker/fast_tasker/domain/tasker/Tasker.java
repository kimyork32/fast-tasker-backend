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

    @Id
    private UUID id;

    /**
     * ID of the account linked to the tasker
     */
    @Column(name = "account_id", unique = true, nullable = false)
    private UUID accountId;

    /**
     * profile data
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", length = 60)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", length = 60)),
            @AttributeOverride(name = "photo", column = @Column(name = "profile_photo_url", length = 255)),
            @AttributeOverride(name = "about", column = @Column(name = "profile_about", length = 200)),
            @AttributeOverride(name = "reputation", column = @Column(name = "profile_reputation")),
            @AttributeOverride(name = "clientReviews", column = @Column(name = "profile_client_reviews")),
            @AttributeOverride(name = "completedTasks", column = @Column(name = "profile_completed_tasks")),

            @AttributeOverride(name = "location.zip", column = @Column(name = "location_zip", nullable = false)),
            @AttributeOverride(name = "location.latitude", column = @Column(name = "location_latitude", nullable = false)),
            @AttributeOverride(name = "location.longitude", column = @Column(name = "location_longitude", nullable = false)),
            @AttributeOverride(name = "location.address", column = @Column(name = "location_address", length = 255, nullable = false))
    })
    private Profile profile;

    @Builder(toBuilder = true)
    public Tasker(UUID accountId, Profile profile) {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }
        if (profile == null) {
            throw new IllegalArgumentException("profile cannot be null");
        }
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.profile = profile;
    }
}