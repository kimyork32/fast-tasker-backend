package com.fasttasker.fast_tasker.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * represent a VO for a password.
 * this class is immutable and its main purpose is to encapsulate
 * the security logic, ALWAYS storing a hash of the password,
 * never the plaintext.
 * @ Embeddable is used so that JPA stores it as columns
 * in the owner entity's table Account.
 */
@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString(exclude = "value") //exclude "value" in logs
public class Password {

    /**
     * never stored the password in plain text.
     */
    @Column(name = "password_hash", nullable = false)
    private String value;

    public Password(String hash) {
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
        this.value = hash;
    }
}