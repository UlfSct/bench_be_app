package ru.bench.app.repository.file;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.file.File;

public interface FileRepository extends JpaRepository<File, Long> {}