package com.fasttasker.fast_tasker.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.regex.Pattern;

/**
 * represents a OV for an email address.
 * this class encapsulates the value of an email address and is immutable.
 * @ Embeddable is used so that JPA stores it as a column.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Email {

    public static final String REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "email", unique = true, nullable = false)
    private String value;

    public Email(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format provided to Domain");
        }
        this.value = value;
    }
}