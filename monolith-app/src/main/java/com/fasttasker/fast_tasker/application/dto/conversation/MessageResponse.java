package com.fasttasker.fast_tasker.application.dto.conversation;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record MessageResponse(
        UUID id,
        UUID senderId,
        MessageContentResponse content,
        Instant sentAt,
        boolean isRead
) {}
