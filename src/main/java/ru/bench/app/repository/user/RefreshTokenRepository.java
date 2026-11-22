package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.user.RefreshToken;
import ru.bench.app.entity.user.UserSession;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllBySessionAndIsUsedFalseAndExpiresAtAfter(UserSession session, java.time.Instant now);
}