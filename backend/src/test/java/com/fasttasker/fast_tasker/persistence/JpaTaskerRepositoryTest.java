package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import com.fasttasker.fast_tasker.persistence.repository.TaskerRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TaskerRepositoryImpl.class))
class JpaTaskerRepositoryTest {
    @Autowired
    ITaskerRepository taskerRepository;

    @Test
    void shouldSaveAndFindATaskerById() {
        // 1. ARRANGE
        UUID taskerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Location location = new Location(
                -16.409047,
                -71.537731,
                "location random",
                4141414
        );

        Profile profile = new Profile(
                "homer",
                "simpson",
                "https://example.com/photo.png",
                location,
                "im tasker expert in plumbing",
                4,
                3,
                10
        );

        Tasker newTasker =  new Tasker(
                taskerId,
                accountId,
                profile
        );

        // 2. ACT
        // save
        taskerRepository.save(newTasker);
        // find
        Optional<Tasker> foundTaskerOpt = taskerRepository.findById(taskerId);

        // 3. ASSERT
        assertThat(foundTaskerOpt)
                .withFailMessage("Tasker with id " + taskerId + " not found")
                .isPresent();

        Tasker  foundTasker = foundTaskerOpt.get();

        assertThat(foundTasker)
                .withFailMessage("the Tasker found IS NOT as the expected one")
                .isEqualTo(newTasker);
    }

    @Test
    void shouldSaveAndFindATaskerByAccountId() {
        // 1. ARRANGE
        UUID taskerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        var location = new Location(
                -16.409047,
                -71.537731,
                "location random",
                41414141
        );

        var profile = new Profile(
                "homer",
                "simpson",
                "https://example.com/photo.png",
                location,
                "im tasker expert in plumbing",
                4,
                3,
                10
        );

        var newTasker =  new Tasker(
                taskerId,
                accountId,
                profile
        );

        // 2. ACT
        // save
        taskerRepository.save(newTasker);
        // find
        Optional<Tasker> foundTaskerOpt = taskerRepository.findByAccountId(accountId);

        // 3. ASSERT
        assertThat(foundTaskerOpt)
                .withFailMessage("Tasker with id " + taskerId + " not found")
                .isPresent();

        Tasker  foundTasker = foundTaskerOpt.get();

        assertThat(foundTasker)
                .withFailMessage("the Tasker found IS NOT as the expected one")
                .isEqualTo(newTasker);
    }
}