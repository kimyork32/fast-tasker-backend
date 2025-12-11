package com.fasttasker.fast_tasker.domain.tasker;

import jakarta.persistence.Column;
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
    @Column(name = "latitude", nullable = false)
    private double latitude;

    /**
     * value between -180 and 180
     */
    @Column(name = "longitude", nullable = false)
    private double longitude;

    /**
     * address of the location ...
     */
    @Column(name = "address", length = 256, nullable = false)
    private String address;

    /**
     * zip code of peru (FOR THE MOMENT)
     */
    @Column(name = "zip")
    private String zip;

    @Builder(toBuilder = true)
    public Location(double latitude, double longitude, String address, String zip) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
        if (zip == null || zip.isEmpty()) {
            throw new IllegalArgumentException("zip cannot be empty");
        }

        // validate address with zip here

        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.zip = zip;
    }

}