package com.fasttasker.fast_tasker.application.exception;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException() {
        super("The password has been incorrect");
    }
}
