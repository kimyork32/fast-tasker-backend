package com.fasttasker.fast_tasker.web.controller;

import com.fasttasker.fast_tasker.application.ConversationService;
import com.fasttasker.fast_tasker.application.dto.conversation.ConversationRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.ConversationSummary;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.StartChatRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
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

    /**
     * Retrieves the user's chat list
     * @param authentication token
     * @return list of conversations
     */
    @GetMapping("/api/v1/conversations/inbox")
    public ResponseEntity<List<ConversationSummary>> getInbox(Authentication authentication) {
        // extract ID of the token
        UUID taskerId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(conversationService.getUserInbox(taskerId));
    }

    /**
     * start a new conversation or securely resume an existing one
     * @param request request from frontend
     * @param authentication token
     * @return conversation id
     */
    @GetMapping("api/v1/conversations/start")
    public ResponseEntity<UUID> startChat(
            @RequestBody StartChatRequest request,
            Authentication authentication
    ) {
        UUID participantA = UUID.fromString(authentication.getName()); // obtain userId from the token
        UUID participantB = request.targetUserId();

        ConversationRequest secureRequest = new ConversationRequest(
                request.taskId(),
                participantA,
                participantB
        );

        return ResponseEntity.ok(conversationService.startConversation(secureRequest));
    }
}
