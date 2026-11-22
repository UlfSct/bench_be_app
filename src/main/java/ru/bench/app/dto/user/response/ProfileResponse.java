package ru.bench.app.dto.user.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProfileResponse(
        UUID uid,
        String username,
        String email,
        String avatarUrl,
        boolean isEmailConfirmed,
        boolean isSignedUserAgreement,
        boolean isSignedPrivacyPolicy,
        LocaleDto locale,
        CountryDto country,
        List<RoleDto> roles,
        List<SessionDto> sessions,
        Instant createdAt
) {}