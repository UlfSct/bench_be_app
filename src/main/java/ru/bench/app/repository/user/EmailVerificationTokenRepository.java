package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.user.EmailVerificationToken;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
}