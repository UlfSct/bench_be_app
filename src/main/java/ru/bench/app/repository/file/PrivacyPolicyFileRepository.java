package ru.bench.app.repository.file;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bench.app.entity.file.PrivacyPolicyFile;

public interface PrivacyPolicyFileRepository extends JpaRepository<PrivacyPolicyFile, Long> {}