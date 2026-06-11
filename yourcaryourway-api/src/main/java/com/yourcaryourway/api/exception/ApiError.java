package com.yourcaryourway.api.exception;

public record ApiError(
        int status,
        String error,
        String message
) {}