package com.fasttasker.fast_tasker.application.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        @Pattern(regexp = com.fasttasker.fast_tasker.domain.account.Email.REGEX, message = "Invalid email format")

        String email,

        @NotBlank(message = "Password cannot be blank")
        String rawPassword
) {}
