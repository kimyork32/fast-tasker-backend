package com.fasttasker.fast_tasker.domain.tasker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    // helper
    private final Location validLocation = Location.builder()
            .latitude(-16.4)
            .longitude(-71.5)
            .zip("04001")
            .build();

    @Test
    @DisplayName("Should create profile and initialize stats to zero")
    void shouldCreateProfileAndInitStats() {
        Profile profile = Profile.builder()
                .firstName("Juan")
                .lastName("Perez")
                .location(validLocation)
                .about("I am a tasker")
                .build();

        assertThat(profile).isNotNull();
        assertThat(profile.getReputation()).isEqualTo(0.0f);
        assertThat(profile.getClientReviews()).isEqualTo(0);
        assertThat(profile.getCompletedTasks()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should throw when firstName is null")
    void shouldThrowWhenFirstNameNull() {
        assertThatThrownBy(() -> Profile.builder()
                .firstName(null)
                .lastName("Perez")
                .location(validLocation)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("firstName and lastName cannot be null");
    }

    @Test
    @DisplayName("Should throw when firstName is empty")
    void shouldThrowWhenFirstNameEmpty() {
        assertThatThrownBy(() -> Profile.builder()
                .firstName("")
                .lastName("Perez")
                .location(validLocation)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("firstName and lastName cannot be empty");
    }

    @Test
    @DisplayName("Should throw when location is null")
    void shouldThrowWhenLocationNull() {
        assertThatThrownBy(() -> Profile.builder()
                .firstName("Juan")
                .lastName("Perez")
                .location(null)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("location cannot be null");
    }

    @Test
    @DisplayName("Should throw when about is too long")
    void shouldThrowWhenAboutTooLong() {
        String longAbout = "a".repeat(201); // string with 201 characters

        assertThatThrownBy(() -> Profile.builder()
                .firstName("Juan")
                .lastName("Perez")
                .location(validLocation)
                .about(longAbout)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("about cannot be longer than 200 characters");
    }
}