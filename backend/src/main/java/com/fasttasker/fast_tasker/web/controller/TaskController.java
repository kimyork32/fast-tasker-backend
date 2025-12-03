package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.TaskService;
import com.fasttasker.fast_tasker.application.dto.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


/**
 * task controller
 */
@RestController
@RequestMapping("api/v1/tasks") // Plural, REST conversion
public class TaskController {

    private final TaskService taskService;
    /**
     * constructor for dependencies injection
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody TaskRequest request,
            Authentication authentication
    ) {
        // extract posterId from the token
        UUID posterId = (UUID) authentication.getPrincipal();

        TaskResponse response = taskService.createTask(request, posterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllPublicActiveTasks() {
        List<TaskResponse> tasks = taskService.listActiveTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskResponse>> getAllMyTasks(Authentication authentication) {
        UUID posterId = (UUID) authentication.getPrincipal();
        List<TaskResponse> posterTasks = taskService.listTasksByPoster(posterId);
        return ResponseEntity.ok(posterTasks);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskCompleteResponse> getTaskComplete(@PathVariable ("taskId") UUID taskId) {
        TaskCompleteResponse taskCompleteResponse = taskService.getTaskCompleteById(taskId);
        return ResponseEntity.ok(taskCompleteResponse);
    }

    @PostMapping("/{taskId}/offers")
    public ResponseEntity<OfferProfileResponse> createOffer(
            @PathVariable UUID taskId,
            @RequestBody OfferRequest offerRequest,
            Authentication authentication
    ) {
        UUID accountId = (UUID) authentication.getPrincipal();
        OfferProfileResponse offerProfileResponse = taskService.createOffer(offerRequest, taskId, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(offerProfileResponse);
    }

    @GetMapping("/{taskId}/offers")
    public ResponseEntity<List<OfferProfileResponse>> getAllOffersByTask(@PathVariable ("taskId") UUID taskId) {
        List<OfferProfileResponse> offers = taskService.listOffersByTask(taskId);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/{taskId}/questions")
    public ResponseEntity<QuestionProfileResponse> createQuestion(
            @PathVariable UUID taskId,
            @RequestBody QuestionRequest questionRequest,
            Authentication authentication
    ) {
        UUID accountId = (UUID) authentication.getPrincipal();
        QuestionProfileResponse response = taskService.createQuestion(questionRequest, taskId, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}