package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.task.*;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.persistence.repository.TaskRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TaskRepositoryImpl.class))
@ActiveProfiles("test")
class JpaTaskRepositoryTest {

    @Autowired
    ITaskRepository taskRepository;

    @Test
    void shouldSaveAndFindYouTaskById() {
        // 1. ARRANGE
        UUID id = UUID.randomUUID();
        Location location = new Location(12.1212, 42.4242, "address location", 4141414);
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
                null,
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
        Instant createdAtOffer = Instant.parse("2020-04-21T05:00:00Z");
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
        Instant askingAtQuestion = Instant.parse("2020-04-21T07:00:00Z");
        Question newQuestion = new Question(
                questionId,
                "description for question",
                QuestionStatus.PENDING,
                askingById,
                askingAtQuestion,
                null,
                null
        );

        // created task
        UUID taskId = UUID.randomUUID();
        Location location = new Location(12.1212, 42.4242, "address location", 414141);
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
                null,
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

        assertThat(foundTask)
                .withFailMessage("the task found IS NOT same as the expected one")
                .isEqualTo(newTask);
    }
}