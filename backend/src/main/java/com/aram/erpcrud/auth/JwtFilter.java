package com.aram.erpcrud.auth;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;

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
            filterChain.doFilter(request, response);
            return;
        }

        // If the token is invalid, continue with the next filter
        if (!jwtHandler.isValidToken(tokenOptional.get())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the username from the token
        // If no username is present, continue with the next filter
        Optional<String> username = jwtHandler.parseUsername(tokenOptional.get());
        if (username.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Load user details
        // If not found, continue with the next filter
        UserDetails userDetails = userDetailsService.loadUserByUsername(username.get());
        if (userDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Set the authentication in the security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue with the next filter
        filterChain.doFilter(request, response);
    }
}