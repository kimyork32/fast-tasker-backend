package com.fasttasker.fast_tasker.application.dto.conversation;

import java.util.UUID;

public record MessageRequest(
        UUID conversationId,
        MessageContentRequest content
        // sentAt should be created in the service, no in frontend
        // readAt is not necessary
) {}
