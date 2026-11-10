package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_users_uid", columnNames = "uid"),
        @UniqueConstraint(name = "uq_users_username", columnNames = "username"),
        @UniqueConstraint(name = "uq_users_email", columnNames = "email")
    }
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "uid", nullable = false)
    @Getter
    private UUID uid;

    @Column(name = "username", nullable = false, length = 50)
    @Getter
    @Setter
    private String username;

    @Column(name = "password_hash", nullable = false, length = 100)
    @Getter
    @Setter
    private String passwordHash;

    @Column(name = "email", nullable = false, length = 255)
    @Getter
    @Setter
    private String email;

    @Column(name = "is_email_confirmed", nullable = false)
    @Getter
    @Setter
    private boolean isEmailConfirmed = false;

    @Column(name = "avatar_url", length = 500)
    @Getter
    @Setter
    private String avatarUrl;

    @Column(name = "is_active", nullable = false)
    @Getter
    @Setter
    private boolean isActive = true;

    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private UserRole role = UserRole.USER;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @Getter
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    @Getter
    private Instant updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private UserSettings settings;

    @PrePersist
    private void assignUid() {
        if (uid == null) {
            uid = UUID.randomUUID();
        }
    }
}
