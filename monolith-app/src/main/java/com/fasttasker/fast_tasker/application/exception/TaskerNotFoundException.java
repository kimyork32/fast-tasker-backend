package com.fasttasker.fast_tasker.application.exception;

import java.util.UUID;

public class TaskerNotFoundException extends RuntimeException {
    public TaskerNotFoundException(String message) {
        super(message);
    }
    public TaskerNotFoundException(UUID id) {
        super("Tasker not found with ID: " + id);
    }
    public static TaskerNotFoundException fromAccountId(UUID accountId) {
        return new TaskerNotFoundException("Tasker not found for accountId: " + accountId);
    }
}