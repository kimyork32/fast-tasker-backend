package com.fasttasker.fast_tasker.application.dto.tasker;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record ProfileRequest(
        @NotBlank(message = "firstName is required")
        // ^[a-zA-ZÀ-ÿ]+ : starts with a word (letters and accents)
        // (?:\\s[a-zA-ZÀ-ÿ]+){0,2}$ : followed by 0 to 2 groups of (space + word)
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÿ]+(?:\\s[a-zA-ZÀ-ÿ]+){0,2}$",
                message = "the firstName must be between 1 and 3 words (letters only)"
        )
        String firstName,

        @NotBlank(message = "lastName is required")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÿ]+(?:\\s[a-zA-ZÀ-ÿ]+){0,2}$",
                message = "the firstName must be between 1 and 3 words (letters only)"
        )
        String lastName,

        @org.hibernate.validator.constraints.URL(message = "the photo must be a valid URL")
        String photo,

        @Size(max = 200, message = "the about cannot be longer than 200 characters")
        String about,

        @Valid
        LocationRequest location
) {
}
