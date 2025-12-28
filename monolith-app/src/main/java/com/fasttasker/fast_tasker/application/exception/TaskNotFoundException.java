package com.fasttasker.fast_tasker.application.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
    public TaskNotFoundException(UUID id) {
        super("Task not found with id: " + id);
    }
}