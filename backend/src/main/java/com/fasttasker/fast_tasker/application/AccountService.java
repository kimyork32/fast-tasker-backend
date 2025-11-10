package com.fasttasker.fast_tasker.application;

import domain.task.ITaskRepository;
import domain.tasker.ITaskerRepository;
import domain.notification.INotificationRepository;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class AccountService {

    /**
     * Default constructor
     */
    public AccountService() {
    }

    /**
     * 
     */
    private ITaskRepository taskRepository;

    /**
     * 
     */
    private ITaskerRepository taskerRepository;

    /**
     * 
     */
    private INotificationRepository notificationRepository;




    /**
     * @param email 
     * @param rawPassword
     */
    public void registerAccount(String email, String rawPassword) {
        // TODO implement here
    }

    /**
     * @param email 
     * @param rawPassword
     */
    public void login(String email, String rawPassword) {
        // TODO implement here
    }

    /**
     * @param accountId 
     * @param oldPass 
     * @param newPass
     */
    public void changePassword(UUID accountId, String oldPass, String newPass) {
        // TODO implement here
    }

    /**
     * @param accountId
     */
    public void activate(UUID accountId) {
        // TODO implement here
    }

    /**
     * @param accountId
     */
    public void ban(UUID accountId) {
        // TODO implement here
    }

    /**
     * @param accountId
     */
    public void getById(UUID accountId) {
        // TODO implement here
    }

}