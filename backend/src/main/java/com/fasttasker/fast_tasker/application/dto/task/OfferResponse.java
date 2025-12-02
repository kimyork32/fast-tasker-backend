package com.fasttasker.fast_tasker.application.dto.task;

import com.fasttasker.fast_tasker.domain.task.OfferStatus;

import java.time.Instant;
import java.util.UUID;

public record OfferResponse(
        UUID id,
        int price,
        String description,
        OfferStatus status,
        Instant createAt
) {}