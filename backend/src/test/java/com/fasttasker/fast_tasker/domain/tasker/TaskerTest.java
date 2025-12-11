package com.fasttasker.fast_tasker.domain.tasker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskerTest {

    private final UUID validAccountId = UUID.randomUUID();
    private final Profile validProfile = Profile.builder()
            .firstName("Test")
            .lastName("User")
            .location(Location.builder().latitude(0).longitude(0).zip("000").build())
            .about("Test")
            .build();

    @Test
    @DisplayName("Should create Tasker with valid data and generate ID")
    void shouldCreateTasker() {
        Tasker tasker = new Tasker(validAccountId, validProfile);

        assertThat(tasker).isNotNull();
        assertThat(tasker.getId()).isNotNull();
        assertThat(tasker.getAccountId()).isEqualTo(validAccountId);
        assertThat(tasker.getProfile()).isEqualTo(validProfile);
    }

    @Test
    @DisplayName("Should throw exception when accountId is null")
    void shouldThrowWhenAccountIdNull() {
        assertThatThrownBy(() -> new Tasker(null, validProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("accountId cannot be null");
    }

    @Test
    @DisplayName("Should create Tasker without profile using factory method")
    void shouldCreateWithoutProfile() {
        Tasker tasker = Tasker.createWithoutProfile(validAccountId);

        assertThat(tasker).isNotNull();
        assertThat(tasker.getId()).isNotNull();
        assertThat(tasker.getAccountId()).isEqualTo(validAccountId);
        assertThat(tasker.getProfile()).isNull();
    }

    @Test
    @DisplayName("Should update profile correctly")
    void shouldUpdateProfile() {
        Tasker tasker = Tasker.createWithoutProfile(validAccountId);

        tasker.updateProfile(validProfile);

        assertThat(tasker.getProfile()).isNotNull();
        assertThat(tasker.getProfile().getFirstName()).isEqualTo("Test");
    }
}