package com.fasttasker.fast_tasker.domain.account;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * represents a OV for an email address.
 * this class encapsulates the value of an email address and is immutable.
 * @ Embeddable is used so that JPA stores it as a column.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Email {

    /**
     * the email address string.
     * validation anotations coould be added hereee.
     */
    private String value;

}