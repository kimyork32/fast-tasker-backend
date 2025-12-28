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

    /**
     * calculate the snippet of the message
     * @return snippet of the message
     */
    public String snippet() {
        if (text != null) {
            return text;
        }
        else if (attachmentUrl != null) {
            return "Foto";
        }
        return "";
    }
}