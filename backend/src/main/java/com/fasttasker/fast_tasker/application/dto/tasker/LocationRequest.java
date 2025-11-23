package com.fasttasker.fast_tasker.application.dto.tasker;

public record LocationRequest(
        double latitude,
        double longitude,
        String address,
        int zip
) {
}
