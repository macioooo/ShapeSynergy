package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.appuser.AppUser;
import com.shapesynergy.dietworkout.appuser.AppUserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomAppUserDetails implements UserDetails {
    private AppUser user;
    public CustomAppUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> user.getAppUserRole().name());
    }

    public String getName() {
        return user.getName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
