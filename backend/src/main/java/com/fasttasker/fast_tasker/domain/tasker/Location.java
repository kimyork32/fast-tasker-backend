package com.fasttasker.fast_tasker.domain.tasker;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * 
 */
@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Location {

    /**
     * value between -90 and 90
     */
    private double latitude;

    /**
     * value between -180 and 180
     */
    private double longitude;

    /**
     * address of the location ...
     */
    private String address;

    /**
     * zip code of peru (FOR THE MOMENT)
     */
    private int zip;

    @Builder(toBuilder = true)
    public Location(double latitude, double longitude, String address, int zip) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
        if (zip < 0) {
            throw new IllegalArgumentException("zip cannot be negative");
        }

        // validate address with zip here

        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.zip = zip;
    }

}