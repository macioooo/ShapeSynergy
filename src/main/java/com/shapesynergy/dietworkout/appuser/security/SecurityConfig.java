package com.shapesynergy.dietworkout.appuser.security;

import com.shapesynergy.dietworkout.appuser.AppUserRole;
import com.shapesynergy.dietworkout.appuser.service.CustomSuccessHandler;
import com.shapesynergy.dietworkout.appuser.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomSuccessHandler customSuccessHandler;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final String[] USER_URLS = {"/user/**", "/userWorkoutPlans/**", "/exercises/**"};
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //needed for h2 database
        http.headers().frameOptions().disable();
        
        http.csrf(c -> c.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/admin").hasAuthority(AppUserRole.ADMIN.name())
                        .requestMatchers(USER_URLS).hasAuthority(AppUserRole.USER.name())
                        .requestMatchers("/**").permitAll())
                .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").successHandler(customSuccessHandler).permitAll().failureUrl("/loginerror"))
                .logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
