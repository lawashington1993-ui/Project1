package com.safetynet.alerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetyNetAlertsApplication {
    // This is the app's starting point. Spring Boot starts the server and loads the application context when we run it.
    public static void main(String[] args) {
        SpringApplication.run(SafetyNetAlertsApplication.class, args);
    }
}