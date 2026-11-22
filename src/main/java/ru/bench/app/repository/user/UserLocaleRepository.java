package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.user.UserLocale;

public interface UserLocaleRepository extends JpaRepository<UserLocale, Long> {
}