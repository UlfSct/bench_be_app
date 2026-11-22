package ru.bench.app.exception.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bench.app.dto.error.ErrorResponse;
import ru.bench.app.dto.error.FieldErrorDto;
import ru.bench.app.dto.error.ValidationErrorResponse;
import ru.bench.app.exception.ApiException;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException ex) {
        String message = messageSource.getMessage(
                ex.getMessageKey(), null, ex.getMessageKey(), LocaleContextHolder.getLocale());
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse(ex.getCode(), message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldErrorDto> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorDto(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(new ValidationErrorResponse("VALIDATION_ERROR", errors));
    }
}