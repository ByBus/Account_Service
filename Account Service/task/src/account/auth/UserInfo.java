package account.auth;

import account.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfo implements UserDetails {
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> rolesAndAuthorities;
    private final int failedLoginAttempts;

    public UserInfo(UserEntity user) {
        email = user.getEmail();
        password = user.getPassword();
        rolesAndAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        this.failedLoginAttempts = user.getFailedLoginAttempts();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMINISTRATOR.withPrefix()))
                || failedLoginAttempts < MAX_LOGIN_ATTEMPTS;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
