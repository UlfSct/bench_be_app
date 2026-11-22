package ru.bench.app.entity.file;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bench.app.entity.user.User;

@Entity
@Table(
    name = "user_avatar_files",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_avatar_files_user_id", columnNames = "user_id"),
        @UniqueConstraint(name = "uq_user_avatar_files_file_id", columnNames = "file_id")
    }
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class UserAvatarFile {

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    @Getter
    @Setter
    private File file;
}