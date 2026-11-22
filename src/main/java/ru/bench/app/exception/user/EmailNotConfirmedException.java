package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class EmailNotConfirmedException extends ApiException {
    public EmailNotConfirmedException() {
        super("EMAIL_NOT_CONFIRMED", "error.email.notconfirmed", HttpStatus.FORBIDDEN);
    }
}