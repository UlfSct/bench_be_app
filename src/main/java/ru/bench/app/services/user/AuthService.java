package ru.bench.app.services.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bench.app.dto.user.request.LoginRequest;
import ru.bench.app.dto.user.request.RefreshRequest;
import ru.bench.app.dto.user.request.RegisterRequest;
import ru.bench.app.dto.user.response.RegisterResponse;
import ru.bench.app.dto.user.response.TokenPairResponse;
import ru.bench.app.entity.user.*;
import ru.bench.app.event.EmailVerificationRequestedEvent;
import ru.bench.app.exception.user.*;
import ru.bench.app.repository.user.*;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final UserLocaleRepository userLocaleRepository;
    private final CountryRepository countryRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException();
        }

        UserLocale locale = userLocaleRepository.findById(request.localeId())
                .filter(UserLocale::isActive)
                .orElseThrow(LocaleNotFoundException::new);
        Country country = countryRepository.findById(request.countryId())
                .orElseThrow(CountryNotFoundException::new);

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setEmailConfirmed(false);
        user.setSignedUserAgreement(request.isSignedUserAgreement());
        user.setSignedPrivacyPolicy(request.isSignedPrivacyPolicy());
        user.setActive(true);

        UserSettings settings = new UserSettings();
        settings.setUser(user);
        settings.setLocale(locale);
        settings.setCountry(country);
        user.setSettings(settings);

        userRepository.save(user);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setEmail(user.getEmail());
        emailVerificationTokenRepository.save(token);

        eventPublisher.publishEvent(new EmailVerificationRequestedEvent(user, token, locale.getCode()));

        return new RegisterResponse(user.getUsername(), user.getEmail());
    }

    @Transactional
    public TokenPairResponse login(LoginRequest req, String ip, String userAgent) {
        User user = userRepository.findByEmail(req.login())
                .or(() -> userRepository.findByUsername(req.login()))
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        if (!user.isActive()) throw new UserDisabledException();
        var pair = sessionService.createSessionAndTokens(user, ip, userAgent);
        return new TokenPairResponse(pair.accessToken(), pair.refreshToken(), pair.accessExpiresInSeconds());
    }

    @Transactional
    public TokenPairResponse refresh(@Valid RefreshRequest req, String ip, String userAgent) {
        RefreshToken rt = refreshTokenRepository.findByToken(req.refreshToken())
                .orElseThrow(RefreshTokenInvalidException::new);
        if (rt.isUsed() || rt.getExpiresAt().isBefore(Instant.now())) {
            throw new RefreshTokenInvalidException();
        }
        UserSession session = rt.getSession();
        if (session.isEnded()) throw new SessionInvalidException();
        session.setLastUsedAt(Instant.now());
        if (ip != null) session.setIpAddress(ip);
        if (userAgent != null) session.setUserAgent(userAgent);
        var pair = sessionService.rotate(session);
        return new TokenPairResponse(pair.accessToken(), pair.refreshToken(), pair.accessExpiresInSeconds());
    }

    @Transactional
    public void logout(Long sessionId) {
        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(SessionInvalidException::new);
        session.setEnded(true);
        session.setEndedAt(Instant.now());
        Instant now = Instant.now();
        refreshTokenRepository.findAllBySessionAndIsUsedFalseAndExpiresAtAfter(session, now)
                .forEach(rt -> { rt.setUsed(true); rt.setUsedAt(now); });
    }
}