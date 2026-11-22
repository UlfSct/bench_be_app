package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class RefreshTokenInvalidException extends ApiException {
    public RefreshTokenInvalidException() {
        super("REFRESH_TOKEN_INVALID", "error.refresh.invalid", HttpStatus.UNAUTHORIZED);
    }
}