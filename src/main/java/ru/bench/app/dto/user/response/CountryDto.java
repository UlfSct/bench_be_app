package ru.bench.app.dto.user.response;

public record CountryDto(
        Long id,
        String code,
        String name,
        boolean isActive
) {}