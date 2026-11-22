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
import ru.bench.app.exception.user.DocumentsNotSignedException;
import ru.bench.app.exception.user.EmailNotConfirmedException;
import ru.bench.app.exception.user.UserRestrictionException;
import ru.bench.app.repository.user.UserRestrictionRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

@Component
public class AccessGuardFilter extends OncePerRequestFilter {

    private final MessageSource messageSource;
    private final UserRestrictionRepository userRestrictionRepository;
    private final Set<String> allowedPaths;

    public AccessGuardFilter(MessageSource messageSource,
                             UserRestrictionRepository userRestrictionRepository,
                             @Value("${app.api-version}") String apiVersion) {
        this.messageSource = messageSource;
        this.userRestrictionRepository = userRestrictionRepository;
        String base = "/api/" + apiVersion;
        this.allowedPaths = Set.of(
                base + "/profile",
                base + "/auth/logout",
                base + "/auth/refresh",
                base + "/auth/login",
                base + "/auth/register",
                base + "/auth/verify",
                base + "/files/agreement",
                base + "/files/privacy-policy"
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)
                || allowedPaths.contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        ApiException violation = null;
        if (!principal.isEmailConfirmed()) {
            violation = new EmailNotConfirmedException();
        } else if (!principal.isSignedUserAgreement() || !principal.isSignedPrivacyPolicy()) {
            violation = new DocumentsNotSignedException();
        } else if (userRestrictionRepository.existsActiveByUserId(principal.getUserId(), Instant.now())) {
            violation = new UserRestrictionException();
        }

        if (violation != null) {
            writeError(response, violation);
            return;
        }
        chain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, ApiException ex) throws IOException {
        String msg = messageSource.getMessage(ex.getMessageKey(), null,
                ex.getMessageKey(), LocaleContextHolder.getLocale());
        response.setStatus(ex.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"code\":\"" + ex.getCode() + "\",\"message\":\""
                        + msg.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}"
        );
    }
}