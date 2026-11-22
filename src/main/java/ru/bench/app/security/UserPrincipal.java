package ru.bench.app.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bench.app.entity.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final UUID uid;
    private final Long sessionId;
    private final String username;
    private final String password;
    private final boolean active;
    private final boolean emailConfirmed;
    private final Set<String> roleCodes;

    public UserPrincipal(User user, Long sessionId) {
        this.userId = user.getId();
        this.uid = user.getUid();
        this.sessionId = sessionId;
        this.username = user.getUsername();
        this.password = user.getPasswordHash();
        this.active = user.isActive();
        this.emailConfirmed = user.isEmailConfirmed();
        this.roleCodes = user.getRoles().stream()
                .map(ur -> ur.getRole().getCode())
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleCodes.stream()
                .map(c -> new SimpleGrantedAuthority("ROLE_" + c))
                .toList();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return active; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return active; }
}