package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.user.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}