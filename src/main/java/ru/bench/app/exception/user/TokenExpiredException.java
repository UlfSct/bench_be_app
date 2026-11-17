package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class TokenExpiredException extends ApiException {
    public TokenExpiredException() {
        super("TOKEN_EXPIRED", "error.token.expired", HttpStatus.GONE);
    }
}
