package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @Getter
    private Instant createdAt;

    @Column(name = "is_signed_user_agreement", nullable = false)
    @Getter
    @Setter
    private boolean isSignedUserAgreement = false;

    @Column(name = "is_signed_privacy_policy", nullable = false)
    @Getter
    @Setter
    private boolean isSignedPrivacyPolicy = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private UserSettings settings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<EmailChangeToken> emailChangeTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<EmailVerificationToken> emailVerificationTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<UserRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<UserSession> sessions = new ArrayList<>();

    @PrePersist
    private void assignUid() {
        if (uid == null) {
            uid = UUID.randomUUID();
        }
    }
}