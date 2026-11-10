package ru.bench.app.dto.error;

import java.util.List;

public record ValidationErrorResponse(
    String code,
    List<FieldErrorDto> errors
) {}