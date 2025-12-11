package com.fasttasker.fast_tasker.application.dto.tasker;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record TaskerRequest(
        @NotBlank(message = "accountId is required")
        String accountId,

        @NotNull(message = "profile is required")
        @Valid
        ProfileRequest profile
){}