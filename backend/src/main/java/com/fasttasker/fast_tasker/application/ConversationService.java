package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.conversation.ConversationRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.ConversationSummary;
import com.fasttasker.fast_tasker.application.mapper.ConversationMapper;
import com.fasttasker.fast_tasker.domain.conversation.Conversation;
import com.fasttasker.fast_tasker.domain.conversation.IConversationRepository;
import com.fasttasker.fast_tasker.domain.conversation.Message;
import com.fasttasker.fast_tasker.domain.conversation.MessageContent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    private final IConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;

    public ConversationService(IConversationRepository conversationRepository, ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
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
     * get user's inbox
     * @param userId user id
     * @return list of conversations
     */
    @Transactional(readOnly = true)
    public List<ConversationSummary> getUserInbox(UUID userId) {
        return conversationRepository.findByParticipantId(userId).stream()
                .map(c -> {
                    // calculate other id
                    UUID otherId = c.getParticipantA().equals(userId) ? c.getParticipantB() : c.getParticipantA();
                    // get last message
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
}
