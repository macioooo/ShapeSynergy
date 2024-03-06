package com.shapesynergy.dietworkout.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
             , FilterChain filterChain)
            throws ServletException, IOException {
        //get jwt token
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        //check jwt token
        if (authHeader == null|| !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        //extracting email from jwt token
        userEmail = jwtService.extractEmail(jwtToken);
    }
}
