package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.user.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(String code);
}