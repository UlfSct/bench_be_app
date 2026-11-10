package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "email_change_tokens",
        uniqueConstraints = @UniqueConstraint(name = "uq_email_change_token_user_id", columnNames = "user_id")
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class EmailChangeToken {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @Column(name = "token", nullable = false)
    @Getter
    private UUID token;

    @Column(name = "new_email", nullable = false, length = 255)
    @Getter
    @Setter
    private String newEmail;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @Getter
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    @Getter
    private Instant expiresAt;

    @Column(name = "used_at")
    @Getter
    @Setter
    private Instant usedAt;

    private static final Duration TTL = Duration.ofHours(1);

    @PrePersist
    void assignDefaults() {
        if (token == null) token = UUID.randomUUID();
        if (createdAt == null) createdAt = Instant.now();
        if (expiresAt == null) expiresAt = createdAt.plus(TTL);
    }
}
