package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super("INVALID_CREDENTIALS", "error.credentials.invalid", HttpStatus.UNAUTHORIZED);
    }
}