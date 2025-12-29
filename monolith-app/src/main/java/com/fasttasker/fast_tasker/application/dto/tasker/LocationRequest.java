package com.fasttasker.fast_tasker.application.dto.tasker;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder(toBuilder = true)
public record LocationRequest(

        @Min(value = -90, message = "latitude must be greater than or equal to -90")
        @Max(value = 90, message = "latitude must be less than or equal to 90")
        double latitude,

        @Min(value = -180, message = "longitude must be greater than or equal to -180")
        @Max(value = 180, message = "longitude must be less than or equal to 180")
        double longitude,

        @NotBlank(message = "address is required")
        String address,

        @Positive(message = "the postal code must be positive")
        @Min(value = 1000, message = "the postal code must be greater than or equal to 1000")
        String zip
) {}