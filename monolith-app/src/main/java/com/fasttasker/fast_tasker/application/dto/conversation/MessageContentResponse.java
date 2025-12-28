package com.fasttasker.fast_tasker.application.dto.conversation;

import lombok.Builder;

@Builder
public record MessageContentResponse(
        String text,
        String attachmentUrl
) {}
