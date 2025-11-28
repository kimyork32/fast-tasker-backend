package com.fasttasker.fast_tasker.application.dto.conversation;

public record MessageContentRequest(
        String text,
        String attachmentUrl
) {}
