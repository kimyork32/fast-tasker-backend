package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.task.*;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.MinimalProfileResponse;
import com.fasttasker.fast_tasker.domain.task.Offer;
import com.fasttasker.fast_tasker.domain.task.Question;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class TaskMapper {
    public Task toEntity(TaskRequest request) {
        if (request == null) return null;

        var loc = request.location();

        return new Task(
                UUID.randomUUID(),
                request.title(),
                request.description(),
                request.budget(),
                new Location(loc.latitude(), loc.longitude(), loc.address(), loc.zip()),
                LocalDate.parse(request.taskDate()),
                TaskStatus.IN_PROGRESS,
                new UUID(0L, 0L), // default UUID value
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public Offer toOfferEntity(OfferRequest request) {
        return new Offer(
                UUID.randomUUID(),
                request.price(),
                request.description(),
                null, // insert status
                null, // insert offerted by id
                null, // insert  create at
                null // insert task
        );
    }

    public Question toQuestionEntity(QuestionRequest request) {
        if (request == null) return null;

        return new Question(
                UUID.randomUUID(),
                request.description(),
                null, // insert status
                null, // insert asked by id
                null, // insert create at
                null, // insert task
                null // instantiate and add answer list
        );
    }

    public TaskResponse toResponse(Task task) {
        if (task == null) return null;

        var location = task.getLocation();

        var locationResponse = LocationResponse.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .build();

        return TaskResponse.builder()
                .id(task.getId().toString())
                .title(task.getTitle())
                .description(task.getDescription())
                .budget(task.getBudget())
                .location(locationResponse)
                .taskDate(task.getTaskDate().toString())
                .status(task.getStatus().name())
                .posterId(task.getPosterId().toString())
                .build();
    }

    public OfferResponse toOfferResponse(Offer offer) {
        if (offer == null) return null;

        return OfferResponse.builder()
                .id(offer.getId().toString())
                .price(offer.getPrice())
                .description(offer.getDescription())
                .status(offer.getStatus().name())
                .createAt(offer.getCreatedAt().toString())
                .build();
    }

    public OfferProfileResponse toOfferProfileResponse(
            OfferResponse offer,
            MinimalProfileResponse profile
    ) {
        return new OfferProfileResponse(offer, profile);
    }

    public TaskCompleteResponse toTaskCompleteResponse(
            Task task,
            MinimalProfileResponse profile
    ) {
        return new TaskCompleteResponse(
                toResponse(task),
                profile,
                task.getOffers().size(),
                task.getQuestions().size()
        );
    }

    public QuestionResponse toQuestionResponse(Question question) {
        if (question == null) return null;

        return QuestionResponse.builder()
                .id(question.getId().toString())
                .description(question.getDescription())
                .status(question.getStatus().name())
                .createAt(question.getCreatedAt().toString())
                .build();
    }

    public QuestionProfileResponse toQuestionProfileResponse(
            QuestionResponse question,
            MinimalProfileResponse profile
    ) {
        return QuestionProfileResponse.builder()
                .question(question)
                .profile(profile)
                .build();
    }
}
