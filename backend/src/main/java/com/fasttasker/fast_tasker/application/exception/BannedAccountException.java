package com.fasttasker.fast_tasker.application.exception;

public class BannedAccountException extends RuntimeException {
    public BannedAccountException() {
        super("your account has been banned");
    }
}