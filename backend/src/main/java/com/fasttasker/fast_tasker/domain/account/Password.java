package com.fasttasker.fast_tasker.domain.account;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * represent a VO for a password.
 * this class is immutable and its main purpose is to encapsulate
 * the security logic, ALWAYS storing a hash of the password,
 * never the plaintext.
 *
 * @ Embeddable is used so that JPA stores it as columns
 * in the owner entity's table Account.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Password {

    /**
     * never stored the password in plain text.
     */
    private String value;

    /**
     *
     * @param rawPassword password in plain text.
     * @return true if the password matches. False otherwise.
     */
    public boolean verify(String rawPassword) {
        // TODO implement the hash verification logic
        // Use BCryptPasswordEncoder
        return false;
    }

}