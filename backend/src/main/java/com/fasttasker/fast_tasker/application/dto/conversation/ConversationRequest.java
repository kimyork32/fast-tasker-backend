package com.fasttasker.fast_tasker.application.dto.conversation;

import java.util.UUID;

public record ConversationRequest(
        UUID taskId,
        UUID otherUserId
) {}
