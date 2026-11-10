package ru.bench.app.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "{field.any.notblank}")
        @Email(message = "{field.email.invalid}")
        @Size(max = 255, message = "{user.register.email.size}")
        String email,

        @NotBlank(message = "{field.any.notblank}")
        @Size(max = 50, message = "{user.register.username.size}")
        @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{2,}$", message = "{user.register.username.invalid}")
        String username,

        @NotBlank(message = "{field.any.notblank}")
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_\\-+=])[A-Za-z0-9!@#$%^&*()_\\-+=]{12,128}$",
            message = "{user.register.password.weak}"
        )
        String password,

        @NotBlank(message = "{field.any.notblank}")
        @Pattern(regexp = "^[a-z]{2}$", message = "{user.register.locale.invalid}")
        String locale
) {}