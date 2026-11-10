package ru.bench.app.dto.error;

public record ErrorResponse(
    String code,
    String message
) {}
