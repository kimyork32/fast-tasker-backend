package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.task.*;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TaskRepositoryImpl.class))
class JpaTaskRepositoryTest {

    @Autowired
    ITaskRepository taskRepository;

    @Test
    void shouldSaveAndFindYouTaskById() {
        // 1. ARRANGE
        UUID id = UUID.randomUUID();
        Location location = new Location(122.1212, 4242.4242, "address location");
        LocalDate date = LocalDate.parse("2020-04-20");
        UUID posterId = UUID.randomUUID();

        Task newTask = new Task(
                id,
                "title for task",
                "description for task",
                200,
                location,
                date,
                TaskStatus.ACTIVE,
                posterId,
                new ArrayList<>(),
                new ArrayList<>()
        );

        // 2. ACT
        taskRepository.save(newTask);
        Optional<Task> foundTaskOpt = taskRepository.findById(id);

        // 3. ASSERT
        assertThat(foundTaskOpt)
                .withFailMessage("Task with id " + id + " not found")
                .isPresent();

        Task foundTask = foundTaskOpt.get();

        System.out.println(newTask.toString());
        System.out.println(foundTask.toString());

        assertThat(foundTask)
                .withFailMessage("the task found IS NOT same as the expected one")
                .isEqualTo(newTask);
    }

    @Test
    void shouldSaveAndFindYouTaskByPosterId() {
        // 1. ARRANGE
        // created offer
        UUID offerId = UUID.randomUUID();
        UUID bidder =  UUID.randomUUID();
        LocalDateTime  createdAtOffer = LocalDate.parse("2020-04-21").atStartOfDay();
        Offer newOffer = new Offer(
                offerId,
                180,
                "i'm offer this mount",
                OfferStatus.PENDING,
                bidder,
                createdAtOffer,
                null
        );

        // created question
        UUID questionId = UUID.randomUUID();
        UUID askingById =  UUID.randomUUID();
        LocalDateTime askingAtQuestion = LocalDate.parse("2020-04-20").atStartOfDay();
        Question newQuestion = new Question(
                questionId,
                "description for question",
                QuestionStatus.PENDING,
                askingById,
                askingAtQuestion,
                null
        );

        // created task
        UUID taskId = UUID.randomUUID();
        Location location = new Location(122.1212, 4242.4242, "address location");
        LocalDate taskDate = LocalDate.parse("2020-04-20");
        UUID posterId = UUID.randomUUID();

        Task newTask = new Task(
                taskId,
                "title for task",
                "description for task",
                200,
                location,
                taskDate,
                TaskStatus.ACTIVE,
                posterId,
                new ArrayList<>(), // need add offer here
                new ArrayList<>()
        );

        newOffer.setTask(newTask);
        newTask.getOffers().add(newOffer);

        newQuestion.setTask(newTask);
        newTask.getQuestions().add(newQuestion);

        // 2. ACT
        // save
        taskRepository.save(newTask);
        // find with id
        Optional<Task> foundTaskOpt = taskRepository.findById(taskId);

        // 3. ASSERT
        assertThat(foundTaskOpt)
                .withFailMessage("Task with id " + taskId + " not found")
                .isPresent();

        Task foundTask = foundTaskOpt.get();

        System.out.println("new task: " + newTask.toString());
        System.out.println("found task: " + foundTask.toString());

        assertThat(foundTask)
                .withFailMessage("the task found IS NOT same as the expected one")
                .isEqualTo(newTask);
    }
}