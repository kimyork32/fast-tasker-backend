package com.fasttasker.fast_tasker.domain.tasker;

import jakarta.persistence.Embeddable;
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
public class Location {

    /**
     * 
     */
    private double latitude;

    /**
     * 
     */
    private double longitude;

    /**
     * 
     */
    private String address;

    /**
     * zip code of peru
     */
    private int zip;

}