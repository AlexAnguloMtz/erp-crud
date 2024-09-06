package com.aram.erpcrud.auth;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private final JwtHandler jwtHandler;

    public JwtFilter(JwtHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

    }
}
