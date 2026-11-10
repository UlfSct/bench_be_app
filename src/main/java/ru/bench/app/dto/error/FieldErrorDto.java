package ru.bench.app.dto.error;

public record FieldErrorDto (
    String field,
    String message
) {}
