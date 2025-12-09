package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

@Builder(toBuilder = true)
public record LocationRequest(
        double latitude,
        double longitude,
        String address,
        int zip
) {
}
