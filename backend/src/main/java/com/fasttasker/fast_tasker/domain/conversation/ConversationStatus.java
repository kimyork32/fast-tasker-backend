package com.fasttasker.fast_tasker.domain.conversation;

public enum ConversationStatus {
    /**
     * Conversation open for send and receiver messages
     */
    ACTIVE,  // Cambiado de OPEN a ACTIVE para coincidir con el código refactorizado
    /**
     * conversation closed, it will be eliminated from the db (?)
     */
    CLOSED,
}