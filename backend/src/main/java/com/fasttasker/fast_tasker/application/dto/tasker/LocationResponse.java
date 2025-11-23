package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

@Builder
public record LocationResponse(
        double latitude,
        double longitude,
        String address,
        int zip
) {
}
