package ru.bench.app.services.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bench.app.entity.user.EmailVerificationToken;
import ru.bench.app.entity.user.User;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailVerificationMailService {

    private final MailSenderService mailSenderService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.api-version}")
    private String apiVersion;

    public void sendVerification(User user, EmailVerificationToken token, String localeCode) {
        String lang = "en".equalsIgnoreCase(localeCode) ? "en" : "ru";
        String subject = "en".equals(lang) ? "Confirm your email" : "Подтверждение email";
        String link = baseUrl + "/api/" + apiVersion + "/auth/verify?token=" + token.getToken();
        Map<String, Object> vars = new HashMap<>();
        vars.put("username", user.getUsername());
        vars.put("link", link);
        mailSenderService.sendAsync(user.getEmail(), subject, "email/verify_" + lang, vars);
    }
}