package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class LocaleNotFoundException extends ApiException {
    public LocaleNotFoundException() {
        super("INVALID_LOCALE", "error.locale.invalid", HttpStatus.BAD_REQUEST);
    }
}
