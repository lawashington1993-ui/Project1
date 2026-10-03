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
    // Logger used to record incoming and outgoing HTTP request details.
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Log the request before the rest of the filter chain is executed.
        logger.info("{} {}", request.getMethod(), request.getRequestURI());

        // Continue processing the request through the application pipeline.
        chain.doFilter(request, response);

        // Log the final HTTP status after the downstream processing completes.
        logger.info("{} {} -> {}", request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}