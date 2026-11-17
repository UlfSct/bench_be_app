package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class InvalidTokenException extends ApiException {
    public InvalidTokenException() {
        super("INVALID_TOKEN", "error.token.invalid", HttpStatus.BAD_REQUEST);
    }
}