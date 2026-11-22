package ru.bench.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.bench.app.entity.user.UserRestriction;

import java.time.Instant;

public interface UserRestrictionRepository extends JpaRepository<UserRestriction, Long> {

    @Query("select count(r) > 0 from UserRestriction r " +
            "where r.user.id = :userId and r.expiresAt > :now")
    boolean existsActiveByUserId(@Param("userId") Long userId, @Param("now") Instant now);
}