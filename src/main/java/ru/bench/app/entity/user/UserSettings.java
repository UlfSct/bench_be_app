package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "user_settings",
    uniqueConstraints = @UniqueConstraint(name = "uq_user_settings_user_id", columnNames = "user_id")
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class UserSettings {

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

    @ManyToOne
    @JoinColumn(name = "locale_id", nullable = false)
    @Getter
    @Setter
    private UserLocale locale;

}
