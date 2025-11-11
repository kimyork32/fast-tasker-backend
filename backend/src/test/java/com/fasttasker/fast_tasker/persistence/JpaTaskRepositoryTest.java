package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.task.ITaskRepository;
import com.fasttasker.fast_tasker.domain.task.Task;
import com.fasttasker.fast_tasker.domain.task.TaskStatus;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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

    }

    @Test
    void findByReceiverTaskerIdAndStatus() {
    }
}