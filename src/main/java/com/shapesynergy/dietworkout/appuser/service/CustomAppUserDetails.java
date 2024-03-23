package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.appuser.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomAppUserDetails implements UserDetails {
    private AppUser user;
    public CustomAppUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user == null || user.getAppUserRole() == null) {
            return Collections.emptyList();
        }
        return List.of(() -> user.getAppUserRole().name());
    }

    public String getName() {
        return user != null ? user.getName() : null;
    }

    public Long getId() {
        return user != null ? user.getId_user() : null;
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return user != null ? user.getEmail() : null;
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

