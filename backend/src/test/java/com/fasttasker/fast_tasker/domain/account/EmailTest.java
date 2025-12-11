package com.fasttasker.fast_tasker.domain.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        String validAddress = "john.doe@fasttasker.com";
        Email email = new Email(validAddress);

        assertThat(email.getValue()).isEqualTo(validAddress);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "user@.com", "@domain.com", "user@domain"})
    void shouldThrowExceptionForInvalidFormats(String invalidAddress) {
        assertThatThrownBy(() -> new Email(invalidAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void shouldThrowExceptionIfNull() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email cannot be null");
    }

    @Test
    void shouldVerifyEquality() {
        Email email1 = new Email("test@tasker.com");
        Email email2 = new Email("test@tasker.com");

        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).hasSameHashCodeAs(email2);
    }
}