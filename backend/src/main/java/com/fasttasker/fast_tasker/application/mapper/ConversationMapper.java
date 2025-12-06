package com.fasttasker.fast_tasker.application.mapper;

import com.fasttasker.fast_tasker.application.dto.conversation.*;
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

        return Conversation.builder()
                .id(UUID.randomUUID())
                .taskId(request.taskId())
                .participantA(request.participantA())
                .participantB(request.participantB())
                .status(ConversationStatus.OPEN)
                .build();
        // add attribute: messages (List>
    }
    public MessageContent toMessageContentEntity(MessageContentRequest request) {
        if (request == null) return null;

        return new MessageContent(
                request.text(),
                request.attachmentUrl()
        );
    }

    public Message toMessageEntity(MessageRequest request) {
        if (request == null) return null;

        MessageContent messageContent = toMessageContentEntity(request.content());

        // add attribute: senderId, sentAt, readAt
        return Message.builder()
                .id(UUID.randomUUID())
                .content(messageContent)
                .build();
    }

    // To response ///////////////////////////////////////////////
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
