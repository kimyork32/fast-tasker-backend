package com.fasttasker.fast_tasker.application.dto.tasker;

import lombok.Builder;

@Builder
public record ChatProfileResponse (
    String id,
    String firstName,
    String lastName,
    String photo
){}