package com.example.hito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig — Cross-Origin Resource Sharing configuration.
 *
 * WHY THIS IS NEEDED:
 * Browsers block requests from one "origin" (domain + port) to another.
 * Your frontend runs on, say, file:// or localhost:5500,
 * but the backend runs on localhost:8080.
 * Without CORS config, the browser will refuse to let the frontend talk to the backend.
 *
 * This config tells the backend: "It is okay to accept requests from these origins."
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // Allow requests from your frontend.
                        // Add your frontend's origin here if it changes.
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
