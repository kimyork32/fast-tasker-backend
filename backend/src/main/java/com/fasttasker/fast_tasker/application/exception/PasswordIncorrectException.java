package com.fasttasker.fast_tasker.application.exception;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException(String message) {
        super(message);
    }
}
