package com.ejemplo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ========================================
 * CLASE PRINCIPAL DE SPRING BOOT
 * ========================================
 *
 * @SpringBootApplication es una anotación de conveniencia que combina:
 *
 * 1. @Configuration: Indica que esta clase puede tener métodos @Bean
 * 2. @EnableAutoConfiguration: Activa la configuración automática de Spring Boot
 * 3. @ComponentScan: Escanea este paquete y subpaquetes buscando componentes
 *
 * Spring Boot automáticamente:
 * - Configura el servidor web embebido (Tomcat)
 * - Configura la conexión a la base de datos
 * - Registra los beans necesarios
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // SpringApplication.run() inicia toda la aplicación
        // Crea el ApplicationContext de Spring
        // Inicia el servidor web embebido
        SpringApplication.run(DemoApplication.class, args);
    }
}
