package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.conversation.ConversationRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageContentResponse;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageRequest;
import com.fasttasker.fast_tasker.application.dto.conversation.MessageResponse;
import com.fasttasker.fast_tasker.domain.conversation.Conversation;
import com.fasttasker.fast_tasker.domain.conversation.ConversationStatus;
import com.fasttasker.fast_tasker.domain.conversation.Message;
import com.fasttasker.fast_tasker.domain.conversation.MessageContent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConversationMapper {

    public Conversation toConversationEntity(ConversationRequest request) {
        if (request == null) return null;

        return new Conversation(
                UUID.randomUUID(),
                request.taskId(),
                request.participantA(),
                request.participantB(),
                null,
                ConversationStatus.OPEN
        );
    }

    public Message toMessageEntity(MessageRequest request) {
        if (request == null) return null;

        var messageContent = new MessageContent(
                request.content().text(),
                request.content().attachmentUrl()
        );

        return new Message(
                UUID.randomUUID(), // WARNING: this not is the ID
                null,
                request.senderId(),
                messageContent,
                null,
                null
        );
    }

    public MessageResponse toMessageResponse(Message message) {
        if (message == null) return null;

        var messageResponse = new MessageContentResponse(
                message.getContent().getText(),
                message.getContent().getAttachmentUrl()
        );

        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .content(messageResponse)
                .sentAt(message.getSentAt())
                .isRead(message.isRead())
                .build();
    }
}
