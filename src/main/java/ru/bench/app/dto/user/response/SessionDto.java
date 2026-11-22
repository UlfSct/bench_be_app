package ru.bench.app.dto.user.response;

import java.time.Instant;

public record SessionDto(
        Long id,
        Instant createdAt,
        Instant lastUsedAt,
        String ipAddress,
        String userAgent,
        boolean current
) {}