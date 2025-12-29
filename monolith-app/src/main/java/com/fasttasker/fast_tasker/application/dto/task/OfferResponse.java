package com.fasttasker.fast_tasker.application.dto.task;

import lombok.Builder;

@Builder
public record OfferResponse(
        String id,
        int price,
        String description,
        String status,
        String createAt
) {}