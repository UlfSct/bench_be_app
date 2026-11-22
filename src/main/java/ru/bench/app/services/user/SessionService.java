package ru.bench.app.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bench.app.entity.user.RefreshToken;
import ru.bench.app.entity.user.User;
import ru.bench.app.entity.user.UserSession;
import ru.bench.app.repository.user.RefreshTokenRepository;
import ru.bench.app.repository.user.UserSessionRepository;
import ru.bench.app.security.JwtService;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserSessionRepository userSessionRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${app.jwt.refresh-ttl-days}")
    private long refreshTtlDays;

    @Transactional
    public TokenPair createSessionAndTokens(User user, String ip, String userAgent) {
        UserSession session = new UserSession();
        session.setUser(user);
        session.setIpAddress(ip);
        session.setUserAgent(userAgent);
        userSessionRepository.save(session);
        return issueTokens(session, user);
    }

    @Transactional
    public TokenPair rotate(UserSession session) {
        Instant now = Instant.now();
        refreshTokenRepository.findAllBySessionAndIsUsedFalseAndExpiresAtAfter(session, now)
                .forEach(rt -> { rt.setUsed(true); rt.setUsedAt(now); });
        return issueTokens(session, session.getUser());
    }

    private TokenPair issueTokens(UserSession session, User user) {
        Set<String> roleCodes = user.getRoles().stream()
                .map(ur -> ur.getRole().getCode())
                .collect(Collectors.toSet());
        String access = jwtService.generateAccessToken(user.getUid(), session.getId(), roleCodes);
        String refresh = UUID.randomUUID().toString() + "-" + UUID.randomUUID();
        RefreshToken rt = new RefreshToken();
        rt.setSession(session);
        rt.setToken(refresh);
        rt.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshTtlDays)));
        refreshTokenRepository.save(rt);
        return new TokenPair(access, refresh, jwtService.getAccessTtlSeconds());
    }

    public record TokenPair(String accessToken, String refreshToken, long accessExpiresInSeconds) {}
}