package ru.bench.app.dto.user.response;

public record TokenPairResponse(
        String accessToken,
        String refreshToken,
        long accessExpiresInSeconds
) {}