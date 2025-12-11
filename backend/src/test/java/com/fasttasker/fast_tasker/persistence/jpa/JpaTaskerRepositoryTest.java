package com.fasttasker.fast_tasker.persistence.jpa;

import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test
 */
@DataJpaTest // loads a db in memory
class JpaTaskerRepositoryTest {

    @Autowired
    private JpaTaskerRepository jpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindByAccountId() {
        // 1. Arrange
        UUID accountId = UUID.randomUUID();
        Tasker tasker = createTaskerEntity(accountId);

        entityManager.persist(tasker);
        entityManager.flush();

        // 2. Act
        Optional<Tasker> found = jpaRepository.findByAccountId(accountId);

        // 3. Assert
        assertThat(found).isPresent();
        assertThat(found.get().getAccountId()).isEqualTo(accountId);
        assertThat(found.get().getProfile().getFirstName()).isEqualTo("Juan");
    }

    @Test
    void shouldReturnEmptyWhenAccountNotFound() {
        Optional<Tasker> found = jpaRepository.findByAccountId(UUID.randomUUID());
        assertThat(found).isEmpty();
    }

    // helper
    private Tasker createTaskerEntity(UUID accountId) {
        Location loc = Location.builder()
                .latitude(-16.0).longitude(-71.0).address("Av. Test").zip("04000").build();

        Profile prof = Profile.builder()
                .firstName("Juan").lastName("Perez").location(loc).about("Bio").build();

        return new Tasker(accountId, prof);
    }
}