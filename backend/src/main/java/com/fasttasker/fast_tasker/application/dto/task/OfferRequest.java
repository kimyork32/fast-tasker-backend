package com.fasttasker.fast_tasker.application.dto.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OfferRequest(
        @NotBlank(message = "Price cannot be empty")
        @Min(value = 5, message = "Price must be greater than or equal to 5")
        @Max(value = 999, message = "Price must be less than or equal to 999")
        int price,

        @NotBlank(message = "Description cannot be empty")
        @Max(value = 500, message = "Description must be less than or equal to 500 characters")
        String description
) {}