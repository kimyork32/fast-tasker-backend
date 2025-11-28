package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByTaskId(UUID taskId);

    boolean existsBytaskId(UUID taskId);

    // the query selects all records where its appears in participantA or participantB
    // @Query use variable names how column, use "nativeQuery = true" for work how sql native
    @Query("SELECT c FROM Conversation c WHERE c.participantA = :userId OR c.participantB = :userId")
    List<Conversation> findByAnyParticipantId(@Param("userId") UUID userId);
}
