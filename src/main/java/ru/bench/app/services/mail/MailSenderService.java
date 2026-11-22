package ru.bench.app.services.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    @Async("mailExecutor")
    public void sendAsync(String to, String subject, String templateName, Map<String, Object> vars) {
        String html = render(templateName, vars);
        int attempt = 0;
        long[] delays = {10, 20, 40};
        while (attempt < 3) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(html, true);
                mailSender.send(message);
                return;
            } catch (Exception e) {
                attempt++;
                if (attempt >= 3) {
                    return;
                }
                try {
                    TimeUnit.SECONDS.sleep(delays[attempt - 1]);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private String render(String templateName, Map<String, Object> vars) {
        Context ctx = new Context();
        ctx.setVariables(vars);
        return templateEngine.process(templateName, ctx);
    }
}