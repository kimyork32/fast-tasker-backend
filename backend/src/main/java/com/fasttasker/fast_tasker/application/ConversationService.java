package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.conversation.ConversationRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.ConversationSummary;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageResponse;
import com.fasttasker.fast_tasker.application.exception.ConversationNotFountException;
import com.fasttasker.fast_tasker.application.mapper.ConversationMapper;
import com.fasttasker.fast_tasker.domain.conversation.Conversation;
import com.fasttasker.fast_tasker.domain.conversation.IConversationRepository;
import com.fasttasker.fast_tasker.domain.conversation.Message;
import com.fasttasker.fast_tasker.domain.conversation.MessageContent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    private final IConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public ConversationService(IConversationRepository conversationRepository, ConversationMapper conversationMapper, SimpMessagingTemplate messagingTemplate) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * initialize a new conversation
     * @param conversationRequest request fron frontend
     * @return conversation id
     */
    @Transactional
    public UUID startConversation(ConversationRequest conversationRequest) {
        // find conversation by taskId. If it exists, return its ID
        // If it doesn't exist, create a new one, save it, and return the new ID
        return conversationRepository.findByTaskId(conversationRequest.taskId())
                .map(Conversation::getId) // if present, get the existing ID
                .orElseGet(() -> { // if not present, create a new conversation
                    Conversation newConversation = conversationMapper.toConversationEntity(conversationRequest);
                    return conversationRepository.save(newConversation).getId();
                });
    }

    /**
     * get tasker's inbox
     * @param taskerId user id
     * @return list of conversations
     */
    @Transactional(readOnly = true)
    public List<ConversationSummary> getUserInbox(UUID taskerId) {
        return conversationRepository.findByParticipantId(taskerId).stream()
                .map(c -> {
                    // calculate other id
                    UUID otherId = c.getParticipantA().equals(taskerId) ? c.getParticipantB() : c.getParticipantA();
                    // get last messageContent
                    MessageContent lastMessageContent = c.getMessages().getLast().getContent();
                    String snippet = "";
                    if (lastMessageContent.getText() != null) {
                        snippet = lastMessageContent.getText();
                    }
                    else if (lastMessageContent.getAttachmentUrl() != null) {
                        snippet = "Foto";
                    }
                    return new ConversationSummary(
                            c.getId(),
                            c.getTaskId(),
                            otherId,
                            snippet
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     *  get message history
     * @param conversationId conversation id
     * @return list of messages
     */
    public List<MessageResponse> getHistory(UUID conversationId) {
        Conversation c = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFountException("Conversation Not Found"));

        return c.getMessages().stream()
                .map(conversationMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    /**
     * processes an incoming message and sends it to subscribed WebSocket clients.
     * @param messageRequest messageRequest the incoming message data containing conversation ID and content
     * @param senderIdFromToken senderIdFromToken the ID of the authenticated user sending the message
     */
    @Transactional
    public void processAndSendMessage(MessageRequest messageRequest, UUID senderIdFromToken) {
        Conversation conversation = conversationRepository.findById(messageRequest.conversationId())
                .orElseThrow(() -> new ConversationNotFountException("Conversation Not Found"));

        MessageContent messageContent = conversationMapper.toMessageContentEntity(messageRequest.content());

        // save message into the conversation
        conversation.sendMessage(senderIdFromToken, messageContent);

        // save conversation in the db
        Conversation savedConversation = conversationRepository.save(conversation);

        // get the last message was created
        Message newMessage = savedConversation.getMessages().getLast();

        // convert last message to DTO
        MessageResponse messageResponse = conversationMapper.toMessageResponse(newMessage);

        // WEBSOCKET is used
        // Only users subscribed to this specific ID will receive the message
        String destination = "/topic/conversation." + messageRequest.conversationId();

        // send JSON to connected clients
        messagingTemplate.convertAndSend(destination, messageResponse);
    }
}