package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class UserRestrictionException extends ApiException {
    public UserRestrictionException() {
        super("USER_RESTRICTED", "error.user.restricted", HttpStatus.FORBIDDEN);
    }
}