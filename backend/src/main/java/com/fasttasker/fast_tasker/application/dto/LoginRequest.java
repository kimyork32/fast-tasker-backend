package com.fasttasker.fast_tasker.application.dto;

/**
 * DOT for login request
  */
public record LoginRequest(
        String email,
        String rawPassword
) {}
