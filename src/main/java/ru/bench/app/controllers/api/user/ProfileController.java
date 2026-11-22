package ru.bench.app.controllers.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bench.app.dto.user.response.ProfileResponse;
import ru.bench.app.security.UserPrincipal;
import ru.bench.app.services.user.ProfileService;

@RestController
@RequestMapping("/api/${app.api-version}/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> get(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(profileService.getProfile(principal.getUserId(), principal.getSessionId()));
    }
}