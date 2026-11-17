package ru.bench.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    protected ApiException(String code, String messageKey, HttpStatus status) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
        this.status = status;
    }

}