package ru.bench.app.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record ConfirmRequest (
    @NotBlank(message = "{field.any.notblank}")
    String token
) {}
