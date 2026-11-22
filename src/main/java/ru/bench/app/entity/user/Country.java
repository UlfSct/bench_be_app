package ru.bench.app.entity.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "countries",
    uniqueConstraints = @UniqueConstraint(name = "uq_countries_code", columnNames = "code")
)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Country {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "code", nullable = false, length = 10)
    @Getter
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    @Getter
    private String name;

    @Column(name = "is_active", nullable = false)
    @Getter
    @Setter
    private boolean isActive = true;
}