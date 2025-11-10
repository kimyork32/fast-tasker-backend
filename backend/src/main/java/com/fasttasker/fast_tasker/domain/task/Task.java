package com.fasttasker.fast_tasker.domain.task;

import com.fasttasker.fast_tasker.domain.tasker.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * 
 */
public class Task {

    /**
     * Default constructor
     */
    public Task() {
    }

    /**
     * 
     */
    private UUID id;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private int budget;

    /**
     * 
     */
    private Location location;

    /**
     * 
     */
    private LocalDate day;

    /**
     * 
     */
    private TaskStatus status;

    /**
     * 
     */
    private List<Question> questions;

    /**
     * 
     */
    private List<Offer> offers;






}