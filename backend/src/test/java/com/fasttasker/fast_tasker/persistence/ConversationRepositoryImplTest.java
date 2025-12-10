package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.conversation.Conversation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit Test
 */
@ExtendWith(MockitoExtension.class) // habilitate Mockito
class ConversationRepositoryImplTest {

    @Mock
    private JpaConversationRepository jpa;

    @InjectMocks
    private ConversationRepositoryImpl repository;

    @Test
    void save_ShouldDelegateToJpa() {
        // 1. ARRANGE
        Conversation conversation = mock(Conversation.class);
        when(jpa.save(conversation)).thenReturn(conversation);

        // 2. ACT
        Conversation result = repository.save(conversation);

        // 3. ASSERT
        assertThat(result).isEqualTo(conversation);
        verify(jpa).save(conversation);
    }

    @Test
    void findById_ShouldReturnConversation() {
        // 1. ARRANGE
        UUID id = UUID.randomUUID();
        Conversation conversation = mock(Conversation.class);
        when(jpa.findById(id)).thenReturn(Optional.of(conversation));

        // 2. ACT
        Optional<Conversation> result = repository.findById(id);

        // 3. ASSERT
        assertThat(result)
        .isPresent()
        .contains(conversation);
        verify(jpa).findById(id);
    }

    @Test
    void findByTaskId_ShouldReturnConversation() {
        // 1. ARRANGE
        UUID taskId = UUID.randomUUID();
        Conversation conversation = mock(Conversation.class);
        when(jpa.findByTaskId(taskId)).thenReturn(Optional.of(conversation));

        // 2. ACT
        Optional<Conversation> result = repository.findByTaskId(taskId);

        // 3. ASSERT
        assertThat(result)
                .isPresent()
                .contains(conversation);
        verify(jpa).findByTaskId(taskId);
    }

    @Test
    void findByParticipantId_ShouldReturnList() {
        // 1. ARRANGE
        UUID participantId = UUID.randomUUID();
        List<Conversation> conversations = List.of(mock(Conversation.class));
        when(jpa.findByAnyParticipantId(participantId)).thenReturn(conversations);

        // 2. ACT
        List<Conversation> result = repository.findByParticipantId(participantId);

        // 3. ASSERT
        assertThat(result).isEqualTo(conversations);
        verify(jpa).findByAnyParticipantId(participantId);
    }

    @Test
    void existsByTaskId_ShouldReturnTrue() {
        // 1. ARRANGE
        UUID taskId = UUID.randomUUID();
        when(jpa.existsBytaskId(taskId)).thenReturn(true);

        // 2. ACT
        boolean result = repository.existsByTaskId(taskId);

        // 3. ASSERT
        assertThat(result).isTrue();
        verify(jpa).existsBytaskId(taskId);
    }
}