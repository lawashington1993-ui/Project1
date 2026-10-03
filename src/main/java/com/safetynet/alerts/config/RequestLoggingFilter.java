package com.safetynet.alerts.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    // This logger records each incoming HTTP request and the status code returned to the client.
    // It is helpful when we need to debug traffic or understand how the app is being used.
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Log the request as soon as it arrives so we know which endpoint was called.
        logger.info("{} {}", request.getMethod(), request.getRequestURI());

        // Let the rest of the Spring pipeline continue normally.
        chain.doFilter(request, response);

        // Log the final HTTP status after the controller and business logic finish processing.
        logger.info("{} {} -> {}", request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}