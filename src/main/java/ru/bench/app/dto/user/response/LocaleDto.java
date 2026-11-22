package ru.bench.app.dto.user.response;

public record LocaleDto(
        Long id,
        String code,
        String name,
        boolean isActive
) {}