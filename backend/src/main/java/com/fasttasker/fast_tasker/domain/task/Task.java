package com.fasttasker.fast_tasker.domain.task;

import domain.tasker.Location;

import java.io.*;
import java.util.*;

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