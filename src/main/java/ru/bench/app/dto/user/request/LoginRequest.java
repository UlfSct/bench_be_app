package ru.bench.app.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "{field.any.notblank}")
        String login,

        @NotBlank(message = "{field.any.notblank}")
        String password
) {}