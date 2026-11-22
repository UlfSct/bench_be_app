package ru.bench.app.controllers.api.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.bench.app.dto.user.request.LoginRequest;
import ru.bench.app.dto.user.request.RefreshRequest;
import ru.bench.app.dto.user.request.RegisterRequest;
import ru.bench.app.dto.user.response.RegisterResponse;
import ru.bench.app.dto.user.response.TokenPairResponse;
import ru.bench.app.security.UserPrincipal;
import ru.bench.app.services.user.AuthService;

@RestController
@RequestMapping("/api/${app.api-version}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private String extractIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        return xff != null ? xff.split(",")[0].trim() : req.getRemoteAddr();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairResponse> login(@Valid @RequestBody LoginRequest req, HttpServletRequest http) {
        return ResponseEntity.ok(authService.login(req, extractIp(http), http.getHeader("User-Agent")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairResponse> refresh(@Valid @RequestBody RefreshRequest req, HttpServletRequest http) {
        return ResponseEntity.ok(authService.refresh(req, extractIp(http), http.getHeader("User-Agent")));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication auth) {
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        authService.logout(principal.getSessionId());
        return ResponseEntity.noContent().build();
    }
}