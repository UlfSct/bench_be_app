package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class EmailAlreadyExistsException extends ApiException {
    public EmailAlreadyExistsException() {
        super("EMAIL_EXISTS", "error.email.exists", HttpStatus.CONFLICT);
    }
}