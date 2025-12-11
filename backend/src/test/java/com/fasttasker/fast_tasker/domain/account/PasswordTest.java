package com.fasttasker.fast_tasker.domain.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PasswordTest {

    @Test
    void shouldCreateValidPassword() {
        String hash = "$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWrn96pzvPnEyezRIaGZnOB3ibJTA2"; // Example BCrypt hash
        Password password = new Password(hash);

        assertThat(password.getValue()).isEqualTo(hash);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionIfEmpty(String emptyHash) {
        assertThatThrownBy(() -> new Password(emptyHash))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password hash cannot be empty");
    }

    @Test
    void shouldThrowExceptionIfNull() {
        assertThatThrownBy(() -> new Password(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void toStringShouldNotRevealHash() {
        String secretHash = "super_secret_hash";
        Password password = new Password(secretHash);

        String stringRepresentation = password.toString();

        assertThat(stringRepresentation)
                .doesNotContain(secretHash)
                .contains("Password");
    }
}