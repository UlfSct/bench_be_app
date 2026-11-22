package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @EntityGraph(attributePaths = {
            "settings", "settings.locale", "settings.country",
            "roles", "roles.role",
            "sessions"
    })
    Optional<User> findWithProfileById(Long id);
}