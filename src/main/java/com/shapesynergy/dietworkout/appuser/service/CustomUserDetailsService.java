package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.appuser.AppUser;
import com.shapesynergy.dietworkout.appuser.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new CustomAppUserDetails(appUser);
    }
}
