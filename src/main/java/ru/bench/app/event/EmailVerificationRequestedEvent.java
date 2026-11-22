package ru.bench.app.event;

import ru.bench.app.entity.user.EmailVerificationToken;
import ru.bench.app.entity.user.User;

public record EmailVerificationRequestedEvent(
        User user,
        EmailVerificationToken token,
        String localeCode
) {}