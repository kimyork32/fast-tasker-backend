package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.task.OfferRequest;
import com.fasttasker.fast_tasker.application.dto.task.OfferResponse;
import com.fasttasker.fast_tasker.application.dto.task.TaskRequest;
import com.fasttasker.fast_tasker.application.dto.task.TaskResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.LocationResponse;
import com.fasttasker.fast_tasker.domain.task.Offer;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import com.fasttasker.fast_tasker.domain.tasker.Location;
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

    public TaskResponse toResponse(Task task) {
        if (task == null) return null;

        var location = task.getLocation();

        var locationResponse = LocationResponse.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .address(location.getAddress())
                .build();

        return new TaskResponse(
                task.getId().toString(),
                task.getTitle(),
                task.getDescription(),
                task.getBudget(),
                locationResponse,
                task.getTaskDate().toString(),
                task.getStatus().name(),
                task.getPosterId().toString()
        );
    }

    public OfferResponse toOfferResponse(Offer offer) {
        if (offer == null) return null;

        return new OfferResponse(
                offer.getId(),
                offer.getPrice(),
                offer.getDescription(),
                offer.getStatus(),
                offer.getCreatedAt()
        );
    }
}
