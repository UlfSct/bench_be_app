package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class LocaleNotFoundException extends ApiException {
    public LocaleNotFoundException() {
        super("LOCALE_NOT_FOUND", "error.locale.notfound", HttpStatus.BAD_REQUEST);
    }
}