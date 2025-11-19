package com.agromarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing)
 *
 * CORS es necesario cuando el frontend (Angular en localhost:4200)
 * hace peticiones al backend (Spring Boot en localhost:8080)
 *
 * Sin esta configuración, el navegador bloqueará las peticiones
 * por seguridad (Same-Origin Policy)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Aplica a todos los endpoints /api/*
                .allowedOrigins(
                        "http://localhost:4200",  // Angular dev server
                        "http://localhost:3000",  // Otros frontends
                        "http://127.0.0.1:4200"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // Cache de preflight requests
    }
}
