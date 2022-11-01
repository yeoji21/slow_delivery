package be.shop.slow_delivery.config.auth;

import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
public class JwtUserDetails implements UserDetails {
    private long id;
    private String role;
    private boolean isAuthenticated;

    public JwtUserDetails(Claims claims) {
        id = (Long) claims.get("seller_id");
        role = (String) claims.get("auth");
        isAuthenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isAuthenticated) {
            return Collections.singletonList(new SimpleGrantedAuthority(role));
        }
        return Collections.singletonList(new SimpleGrantedAuthority(null));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
