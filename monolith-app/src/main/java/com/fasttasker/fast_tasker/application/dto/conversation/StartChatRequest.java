package com.fasttasker.fast_tasker.application.dto.conversation;

import java.util.UUID;

public record StartChatRequest(
        UUID taskId,
        UUID targetUserId // the ID of the "other" tasker or client
) {}
