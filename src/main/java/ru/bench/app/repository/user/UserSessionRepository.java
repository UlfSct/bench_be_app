package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.user.User;
import ru.bench.app.entity.user.UserSession;

import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    List<UserSession> findAllByUserAndIsEndedFalse(User user);
}