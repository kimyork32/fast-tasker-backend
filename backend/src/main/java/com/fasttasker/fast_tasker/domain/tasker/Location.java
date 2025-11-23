package com.fasttasker.fast_tasker.domain.tasker;

import jakarta.persistence.Embeddable;
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