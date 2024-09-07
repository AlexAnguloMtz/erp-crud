package com.aram.erpcrud.auth.config;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private final JwtHandler jwtHandler;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtHandler jwtHandler, UserDetailsService userDetailsService) {
        this.jwtHandler = jwtHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        // Extract the JWT token from the request header
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Optional<String> tokenOptional = jwtHandler.readToken(httpRequest.getHeader("Authorization"));

        // If no token is present, continue with the next filter
        if (tokenOptional.isEmpty()) {
            log.error("token optional was empty");
            filterChain.doFilter(request, response);
            return;
        }

        // If the token is invalid, continue with the next filter
        if (!jwtHandler.isValidToken(tokenOptional.get())) {
            log.error("token not valid");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the username from the token
        // If no username is present, continue with the next filter
        Optional<String> subject = jwtHandler.parseSubject(tokenOptional.get());
        if (subject.isEmpty()) {
            log.error("username not present in token");
            filterChain.doFilter(request, response);
            return;
        }

        // Load user details
        // If not found, continue with the next filter
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject.get());
        if (userDetails == null) {
            log.error("user details could not find a user for the given subject");
            filterChain.doFilter(request, response);
            return;
        }

        // Create the Authentication that will be stored in the Security Context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.error("all correct. populated spring security context and passing to next filter.");
        // Continue with the next filter
        filterChain.doFilter(request, response);
    }
}