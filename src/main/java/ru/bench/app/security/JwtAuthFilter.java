package ru.bench.app.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.bench.app.entity.user.User;
import ru.bench.app.entity.user.UserSession;
import ru.bench.app.repository.user.UserRepository;
import ru.bench.app.repository.user.UserSessionRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        try {
            Claims claims = jwtService.parse(token);
            Long sessionId = claims.get("sid", Long.class);
            UserSession session = userSessionRepository.findById(sessionId).orElse(null);
            if (session == null || session.isEnded()) {
                chain.doFilter(request, response);
                return;
            }
            User user = session.getUser();
            if (user == null || !user.isActive()) {
                chain.doFilter(request, response);
                return;
            }
            UserPrincipal principal = new UserPrincipal(user, sessionId);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ignored) {
            // invalid token → no auth
        }
        chain.doFilter(request, response);
    }
}