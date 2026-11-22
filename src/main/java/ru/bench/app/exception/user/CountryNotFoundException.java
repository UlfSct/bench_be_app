package ru.bench.app.exception.user;

import org.springframework.http.HttpStatus;
import ru.bench.app.exception.ApiException;

public class CountryNotFoundException extends ApiException {
    public CountryNotFoundException() {
        super("COUNTRY_NOT_FOUND", "error.country.notfound", HttpStatus.BAD_REQUEST);
    }
}