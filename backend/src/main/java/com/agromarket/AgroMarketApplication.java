package com.agromarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación AgroMarket
 *
 * @SpringBootApplication combina tres anotaciones:
 * - @Configuration: Marca la clase como fuente de definiciones de beans
 * - @EnableAutoConfiguration: Habilita la auto-configuración de Spring Boot
 * - @ComponentScan: Busca componentes en este paquete y subpaquetes
 */
@SpringBootApplication
public class AgroMarketApplication {

    public static void main(String[] args) {
        // Inicia la aplicación Spring Boot
        SpringApplication.run(AgroMarketApplication.class, args);

        System.out.println("\n" +
            "╔═══════════════════════════════════════════════╗\n" +
            "║     AGROMARKET BACKEND INICIADO               ║\n" +
            "║                                               ║\n" +
            "║  API REST:     http://localhost:8080/api      ║\n" +
            "║  H2 Console:   http://localhost:8080/h2-console║\n" +
            "╚═══════════════════════════════════════════════╝\n"
        );
    }
}
