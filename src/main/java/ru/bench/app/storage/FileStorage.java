package ru.bench.app.storage;

import java.io.InputStream;

public interface FileStorage {
    String store(String key, InputStream content, long size);
    InputStream load(String key);
    void delete(String key);
}