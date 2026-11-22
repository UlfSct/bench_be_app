package ru.bench.app.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.bench.app.services.mail.EmailVerificationMailService;

@Component
@RequiredArgsConstructor
public class EmailVerificationListener {

    private final EmailVerificationMailService emailVerificationMailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(EmailVerificationRequestedEvent event) {
        emailVerificationMailService.sendVerification(event.user(), event.token(), event.localeCode());
    }
}