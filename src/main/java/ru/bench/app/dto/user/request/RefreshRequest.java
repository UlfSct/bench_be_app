package ru.bench.app.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "{field.any.notblank}")
        String refreshToken
) {}