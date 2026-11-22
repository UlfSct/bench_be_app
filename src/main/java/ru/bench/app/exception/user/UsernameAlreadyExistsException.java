package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException() {
        super("USERNAME_EXISTS", "error.username.exists", HttpStatus.CONFLICT);
    }
}