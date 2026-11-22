package ru.bench.app.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bench.app.dto.user.response.*;
import ru.bench.app.entity.user.User;
import ru.bench.app.entity.user.UserSession;
import ru.bench.app.repository.user.UserSessionRepository;
import ru.bench.app.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId, Long currentSessionId) {
        User user = userRepository.findWithProfileById(userId).orElseThrow();
        List<UserSession> activeSessions = user.getSessions().stream()
                .filter(s -> !s.isEnded())
                .toList();
        return new ProfileResponse(
                user.getUid(),
                user.getUsername(),
                user.getEmail(),
                user.isEmailConfirmed(),
                user.isSignedUserAgreement(),
                user.isSignedPrivacyPolicy(),
                new LocaleDto(
                        user.getSettings().getLocale().getId(),
                        user.getSettings().getLocale().getCode(),
                        user.getSettings().getLocale().getName(),
                        user.getSettings().getLocale().isActive()),
                new CountryDto(
                        user.getSettings().getCountry().getId(),
                        user.getSettings().getCountry().getCode(),
                        user.getSettings().getCountry().getName(),
                        user.getSettings().getCountry().isActive()),
                user.getRoles().stream()
                        .map(ur -> new RoleDto(ur.getRole().getId(), ur.getRole().getCode(), ur.getRole().getName()))
                        .toList(),
                activeSessions.stream()
                        .map(s -> new SessionDto(
                                s.getId(),
                                s.getCreatedAt(),
                                s.getLastUsedAt(),
                                s.getIpAddress(),
                                s.getUserAgent(),
                                s.getId().equals(currentSessionId)))
                        .toList(),
                user.getCreatedAt()
        );
    }
}