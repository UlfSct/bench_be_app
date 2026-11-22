package ru.bench.app.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class LocalFileStorage implements FileStorage {

    private final Path basePath;

    public LocalFileStorage(@Value("${app.storage.local.base-path}") String basePath) {
        this.basePath = Path.of(basePath);
        try {
            Files.createDirectories(this.basePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String store(String key, InputStream content, long size) {
        Path target = basePath.resolve(key).normalize();
        if (!target.startsWith(basePath)) throw new IllegalArgumentException("Invalid key");
        try {
            Files.createDirectories(target.getParent());
            Files.copy(content, target, StandardCopyOption.REPLACE_EXISTING);
            return key;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public InputStream load(String key) {
        Path target = basePath.resolve(key).normalize();
        if (!target.startsWith(basePath)) throw new IllegalArgumentException("Invalid key");
        try {
            return Files.newInputStream(target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(basePath.resolve(key).normalize());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}