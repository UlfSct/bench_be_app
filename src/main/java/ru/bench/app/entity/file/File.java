package ru.bench.app.entity.file;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "files",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_files_uid", columnNames = "uid"),
        @UniqueConstraint(name = "uq_files_storage_key", columnNames = "storage_key")
    }
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class File {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "uid", nullable = false)
    @Getter
    private UUID uid;

    @Column(name = "original_name", nullable = false, length = 255)
    @Getter
    @Setter
    private String originalName;

    @Column(name = "content_type", nullable = false, length = 100)
    @Getter
    @Setter
    private String contentType;

    @Column(name = "size", nullable = false)
    @Getter
    @Setter
    private long size;

    @Column(name = "storage_key", nullable = false, length = 500)
    @Getter
    @Setter
    private String storageKey;

    @Column(name = "created_at", nullable = false)
    @Getter
    private Instant createdAt;

    @Column(name = "is_deleted", nullable = false)
    @Getter
    @Setter
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    @Getter
    @Setter
    private Instant deletedAt;

    @PrePersist
    void assignDefaults() {
        if (uid == null) uid = UUID.randomUUID();
        if (createdAt == null) createdAt = Instant.now();
    }
}