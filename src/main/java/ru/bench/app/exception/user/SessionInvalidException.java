package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class SessionInvalidException extends ApiException {
    public SessionInvalidException() {
        super("SESSION_INVALID", "error.session.invalid", HttpStatus.UNAUTHORIZED);
    }
}