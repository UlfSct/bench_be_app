package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class UserDisabledException extends ApiException {
    public UserDisabledException() {
        super("USER_DISABLED", "error.user.disabled", HttpStatus.FORBIDDEN);
    }
}