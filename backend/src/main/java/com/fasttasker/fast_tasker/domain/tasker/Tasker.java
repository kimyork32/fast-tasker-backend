package com.fasttasker.fast_tasker.domain.tasker;


import com.fasttasker.fast_tasker.domain.task.Task;

import java.util.List;
import java.util.UUID;

/**
 * 
 */
public class Tasker {

    /**
     * Default constructor
     */
    public Tasker() {
    }

    /**
     * 
     */
    private UUID id;

    /**
     * 
     */
    private UUID accountId;

    /**
     * 
     */
    private Profile profile;

    /**
     * 
     */
    private List<Task> tasks;



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