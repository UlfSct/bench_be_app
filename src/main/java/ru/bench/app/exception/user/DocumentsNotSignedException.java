package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class DocumentsNotSignedException extends ApiException {
    public DocumentsNotSignedException() {
        super("DOCUMENTS_NOT_SIGNED", "error.documents.notsigned", HttpStatus.FORBIDDEN);
    }
}