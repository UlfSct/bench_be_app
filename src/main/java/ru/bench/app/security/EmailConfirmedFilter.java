package ru.bench.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.bench.app.exception.ApiException;
import ru.bench.app.exception.user.EmailNotConfirmedException;

import java.io.IOException;
import java.util.Set;

@Component
public class EmailConfirmedFilter extends OncePerRequestFilter {

    private final MessageSource messageSource;
    private final Set<String> allowedPaths;

    public EmailConfirmedFilter(MessageSource messageSource,
                                @Value("${app.api-version}") String apiVersion) {
        this.messageSource = messageSource;
        String base = "/api/" + apiVersion;
        this.allowedPaths = Set.of(
                base + "/profile",
                base + "/auth/logout",
                base + "/auth/refresh",
                base + "/auth/login",
                base + "/auth/register",
                base + "/auth/verify"
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal
                && !principal.isEmailConfirmed()
                && !allowedPaths.contains(request.getRequestURI())) {

            ApiException ex = new EmailNotConfirmedException();
            String msg = messageSource.getMessage(ex.getMessageKey(), null,
                    ex.getMessageKey(), LocaleContextHolder.getLocale());

            response.setStatus(ex.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"code\":\"" + ex.getCode() + "\",\"message\":\""
                            + msg.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}"
            );
            return;
        }
        chain.doFilter(request, response);
    }
}