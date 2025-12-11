package com.fasttasker.fast_tasker.persistence.repository;

import com.fasttasker.fast_tasker.application.exception.TaskerNotFoundException;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import com.fasttasker.fast_tasker.persistence.jpa.JpaTaskerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskerRepositoryImplTest {

    @Mock
    private JpaTaskerRepository jpaRepository;

    @InjectMocks
    private TaskerRepositoryImpl taskerRepository;

    @Test
    void shouldSaveTasker() {
        Tasker tasker = mock(Tasker.class);
        when(jpaRepository.save(tasker)).thenReturn(tasker);

        Tasker result = taskerRepository.save(tasker);

        assertThat(result).isEqualTo(tasker);
        verify(jpaRepository).save(tasker);
    }

    @Test
    void shouldReturnTaskerWhenAccountFound() {
        UUID accountId = UUID.randomUUID();
        Tasker expectedTasker = mock(Tasker.class);

        // simulating that JPA find something
        when(jpaRepository.findByAccountId(accountId)).thenReturn(Optional.of(expectedTasker));

        Tasker result = taskerRepository.findByAccountId(accountId);

        assertThat(result).isEqualTo(expectedTasker);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        UUID accountId = UUID.randomUUID();

        // simulating that JPA find nothing
        when(jpaRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        // verifying that the adapter throw exception
        assertThatThrownBy(() -> taskerRepository.findByAccountId(accountId))
                .isInstanceOf(TaskerNotFoundException.class)
                .hasMessageContaining(accountId.toString());
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskerRepository.findById(id))
                .isInstanceOf(TaskerNotFoundException.class);
    }
}