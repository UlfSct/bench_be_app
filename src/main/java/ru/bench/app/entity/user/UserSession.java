package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_sessions")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class UserSession {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @Column(name = "created_at", nullable = false)
    @Getter
    private Instant createdAt;

    @Column(name = "last_used_at", nullable = false)
    @Getter
    @Setter
    private Instant lastUsedAt;

    @Column(name = "ip_address", nullable = false, length = 45)
    @Getter
    @Setter
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    @Getter
    @Setter
    private String userAgent;

    @Column(name = "is_ended", nullable = false)
    @Getter
    @Setter
    private boolean isEnded = false;

    @Column(name = "ended_at")
    @Getter
    @Setter
    private Instant endedAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @PrePersist
    void assignDefaults() {
        if (createdAt == null) createdAt = Instant.now();
        if (lastUsedAt == null) lastUsedAt = createdAt;
    }
}