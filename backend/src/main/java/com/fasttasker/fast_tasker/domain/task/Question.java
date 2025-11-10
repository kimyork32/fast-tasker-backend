package com.fasttasker.fast_tasker.domain.task;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Question {

    /**
     * Default constructor
     */
    public Question() {
    }

    /**
     * 
     */
    private UUID id;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private QuestionStatus status;

    /**
     * 
     */
    private UUID taskId;

    /**
     * 
     */
    private UUID askedBy;

    /**
     * 
     */
    private LocalDateTime date;



}