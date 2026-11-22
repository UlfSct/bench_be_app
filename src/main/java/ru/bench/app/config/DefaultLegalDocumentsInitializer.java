package ru.bench.app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.bench.app.entity.file.File;
import ru.bench.app.entity.file.PrivacyPolicyFile;
import ru.bench.app.entity.file.UserAgreementFile;
import ru.bench.app.entity.user.User;
import ru.bench.app.repository.file.FileRepository;
import ru.bench.app.repository.file.PrivacyPolicyFileRepository;
import ru.bench.app.repository.file.UserAgreementFileRepository;
import ru.bench.app.repository.user.UserRepository;
import ru.bench.app.storage.FileStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DefaultLegalDocumentsInitializer implements ApplicationRunner {

    private final FileRepository fileRepository;
    private final UserAgreementFileRepository userAgreementFileRepository;
    private final PrivacyPolicyFileRepository privacyPolicyFileRepository;
    private final UserRepository userRepository;
    private final FileStorage fileStorage;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) return;

        if (userAgreementFileRepository.count() == 0) {
            UserAgreementFile uaf = new UserAgreementFile();
            uaf.setFile(storeFile("legal/user-agreement.pdf", "user-agreement.pdf"));
            uaf.setCreatedBy(admin);
            userAgreementFileRepository.save(uaf);
        }

        if (privacyPolicyFileRepository.count() == 0) {
            PrivacyPolicyFile ppf = new PrivacyPolicyFile();
            ppf.setFile(storeFile("legal/privacy-policy.pdf", "privacy-policy.pdf"));
            ppf.setCreatedBy(admin);
            privacyPolicyFileRepository.save(ppf);
        }
    }

    private File storeFile(String classpathPath, String originalName) throws IOException {
        ClassPathResource resource = new ClassPathResource(classpathPath);
        String storageKey = "legal/" + UUID.randomUUID() + "-" + originalName;
        try (InputStream is = resource.getInputStream()) {
            fileStorage.store(storageKey, is, resource.contentLength());
        }
        File file = new File();
        file.setOriginalName(originalName);
        file.setContentType("application/pdf");
        file.setSize(resource.contentLength());
        file.setStorageKey(storageKey);
        return fileRepository.save(file);
    }
}