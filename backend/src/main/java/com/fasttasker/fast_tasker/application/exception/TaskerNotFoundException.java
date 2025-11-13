package com.fasttasker.fast_tasker.application.exception;

public class TaskerNotFoundException extends RuntimeException {
    public TaskerNotFoundException(String message) {
        super(message);
    }
}
