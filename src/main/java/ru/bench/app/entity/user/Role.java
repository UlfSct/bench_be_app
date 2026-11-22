package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "roles",
    uniqueConstraints = @UniqueConstraint(name = "uq_roles_code", columnNames = "code")
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Role {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "code", nullable = false, length = 20)
    @Getter
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    @Getter
    private String name;
}