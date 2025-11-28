package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.ConversationService;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

// reference: https://dev.to/supernovabirth/building-a-real-time-chatroom-with-spring-boot-and-websockets-3pk3
// but this is to production, the "reference" is like helloWorld in webSockets
@RestController
public class ChatController {

    private final ConversationService conversationService;

    public ChatController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * receive message from the client
     * part WEBSOCKETS (real time)
     * @param messageRequest messageRequest
     */
    @MessageMapping("/chat/.send")
    public void sendMessage(@Payload MessageRequest messageRequest, Principal principal) {
        // extract ID of the token
        UUID senderId = UUID.fromString(principal.getName());
        conversationService.processAndSendMessage(messageRequest, senderId);
    }
}
