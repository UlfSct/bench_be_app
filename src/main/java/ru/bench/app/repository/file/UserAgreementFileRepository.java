package ru.bench.app.repository.file;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.file.UserAgreementFile;

public interface UserAgreementFileRepository extends JpaRepository<UserAgreementFile, Long> {}