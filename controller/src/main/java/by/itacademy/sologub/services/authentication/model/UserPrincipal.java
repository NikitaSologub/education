package by.itacademy.sologub.services.authentication.model;

import by.itacademy.sologub.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class UserPrincipal implements UserDetails {
    public static final String PREFIX = "ROLE_";
    private final Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
        String singleRole = user.getClass().getSimpleName().toUpperCase(Locale.ROOT);
        authorities.add(new SimpleGrantedAuthority(singleRole));
        authorities.add(new SimpleGrantedAuthority(PREFIX + singleRole));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return user.getCredential().getLogin();
    }

    @Override
    public String getPassword() {
        return user.getCredential().getPassword();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}