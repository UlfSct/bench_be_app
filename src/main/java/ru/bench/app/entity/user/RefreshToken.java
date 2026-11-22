package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "refresh_tokens",
    uniqueConstraints = @UniqueConstraint(name = "uq_refresh_tokens_token", columnNames = "token")
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @Getter
    @Setter
    private UserSession session;

    @Column(name = "token", nullable = false, length = 512)
    @Getter
    @Setter
    private String token;

    @Column(name = "expires_at", nullable = false)
    @Getter
    @Setter
    private Instant expiresAt;

    @Column(name = "is_used", nullable = false)
    @Getter
    @Setter
    private boolean isUsed = false;

    @Column(name = "used_at")
    @Getter
    @Setter
    private Instant usedAt;
}