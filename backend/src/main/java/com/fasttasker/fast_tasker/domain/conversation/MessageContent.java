package com.fasttasker.fast_tasker.domain.conversation;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
  Content of the message
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class MessageContent {

    /**
     * text of the message
     */
    private String text;

    /**
     * attachment url in the message, it can be an image url o video url
     */
    private String attachmentUrl;
}
