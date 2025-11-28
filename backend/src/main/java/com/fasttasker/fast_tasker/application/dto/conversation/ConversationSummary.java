package com.fasttasker.fast_tasker.application.dto.conversation;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ConversationSummary(
        UUID conversationId,
        UUID taskId,
        UUID otherParticipantId,
        String lastMessageSnippet
) {}
