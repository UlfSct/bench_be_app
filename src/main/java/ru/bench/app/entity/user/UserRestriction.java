package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_restrictions")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class UserRestriction {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    @Getter
    @Setter
    private User createdBy;

    @Column(name = "reason", nullable = false, length = 500)
    @Getter
    @Setter
    private String reason;

    @Column(name = "created_at", nullable = false)
    @Getter
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    @Getter
    @Setter
    private Instant expiresAt;

    @PrePersist
    void assignDefaults() {
        if (createdAt == null) createdAt = Instant.now();
    }
}