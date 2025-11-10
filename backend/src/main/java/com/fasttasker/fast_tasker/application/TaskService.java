package com.fasttasker.fast_tasker.application;

import domain.task.ITaskRepository;
import domain.tasker.ITaskerRepository;
import domain.notification.INotificationRepository;
import domain.task.TaskStatus;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class TaskService {

    /**
     * Default constructor
     */
    public TaskService() {
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
     * 
     */
    public void createTask() {
        // TODO implement here
    }

    /**
     * 
     */
    public void editTask() {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void publishTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param taskedId 
     * @param price 
     * @param description
     */
    public void createOffer(UUID taskId, UUID taskedId, int price, String description) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param offerId
     */
    public void acceptOffer(UUID taskId, UUID offerId) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param askerId 
     * @param question
     */
    public void askQuestion(UUID taskId, UUID askerId, String question) {
        // TODO implement here
    }

    /**
     * @param taskId 
     * @param questionId 
     * @param answer
     */
    public void answerQuestion(UUID taskId, UUID questionId, String answer) {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void completeTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void cancelTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * 
     */
    public void listActiveTasks() {
        // TODO implement here
    }

    /**
     * @param city
     */
    public void listByLocation(String city) {
        // TODO implement here
    }

    /**
     * @param status
     */
    public void listByStatus(TaskStatus status) {
        // TODO implement here
    }

}