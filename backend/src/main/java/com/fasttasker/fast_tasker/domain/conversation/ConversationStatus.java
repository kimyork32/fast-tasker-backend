package com.fasttasker.fast_tasker.domain.conversation;

public enum ConversationStatus {
    /**
     * Conversation open for send and receiver messages
     */
    OPEN,
    /**
     * conversation closed, it will be eliminated from the db (?)
     */
    CLOSED,
}
