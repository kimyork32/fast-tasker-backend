package com.fasttasker.fast_tasker.application.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Login exception: invalid password");
    }
}