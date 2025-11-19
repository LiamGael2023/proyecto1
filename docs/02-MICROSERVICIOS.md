# Guía de Microservicios

## ¿Qué son los Microservicios?

Los microservicios son un estilo arquitectónico donde una aplicación se construye como un conjunto de servicios pequeños e independientes.

## Monolito vs Microservicios

### Monolito
```
┌─────────────────────────────────┐
│         APLICACIÓN              │
│  ┌─────┐ ┌─────┐ ┌─────────┐   │
│  │ UI  │ │ API │ │ Negocio │   │
│  └─────┘ └─────┘ └─────────┘   │
│          ┌─────────┐           │
│          │   BD    │           │
│          └─────────┘           │
└─────────────────────────────────┘
```

### Microservicios
```
┌──────────┐  ┌──────────┐  ┌──────────┐
│ Usuarios │  │ Productos│  │ Pedidos  │
│  ┌────┐  │  │  ┌────┐  │  │  ┌────┐  │
│  │ API│  │  │  │ API│  │  │  │ API│  │
│  └────┘  │  │  └────┘  │  │  └────┘  │
│  ┌────┐  │  │  ┌────┐  │  │  ┌────┐  │
│  │ BD │  │  │  │ BD │  │  │  │ BD │  │
│  └────┘  │  │  └────┘  │  │  └────┘  │
└──────────┘  └──────────┘  └──────────┘
```

## Características

1. **Independientes**: Cada servicio se despliega por separado
2. **Especializados**: Cada uno hace una cosa bien
3. **Escalables**: Se escala solo lo necesario
4. **Tecnología flexible**: Cada servicio puede usar diferente stack
5. **Resilientes**: Fallo de uno no tumba todo

## Componentes Clave en Spring Cloud

### 1. API Gateway (Spring Cloud Gateway)
Punto de entrada único para todos los servicios.

```java
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
```

### 2. Service Discovery (Eureka)
Registro donde los servicios se registran y descubren entre sí.

```yaml
# application.yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

### 3. Config Server
Configuración centralizada para todos los servicios.

### 4. Circuit Breaker (Resilience4j)
Manejo de fallos y timeouts entre servicios.

```java
@CircuitBreaker(name = "inventario", fallbackMethod = "fallback")
public Producto obtenerProducto(Long id) {
    return inventarioClient.getProducto(id);
}

public Producto fallback(Long id, Exception e) {
    return new Producto("No disponible", 0);
}
```

## Comunicación entre Servicios

### Síncrona (REST/HTTP)
```java
@FeignClient(name = "productos-service")
public interface ProductoClient {
    @GetMapping("/api/productos/{id}")
    Producto obtenerProducto(@PathVariable Long id);
}
```

### Asíncrona (Mensajería)
```java
// Publicador
rabbitTemplate.convertAndSend("pedidos.exchange", "pedido.creado", pedido);

// Consumidor
@RabbitListener(queues = "pedidos.queue")
public void procesarPedido(Pedido pedido) {
    // procesar...
}
```

## Ejemplo de Arquitectura

```
                    ┌─────────────┐
                    │   Cliente   │
                    └──────┬──────┘
                           │
                    ┌──────┴──────┐
                    │ API Gateway │
                    │   :8080     │
                    └──────┬──────┘
           ┌───────────────┼───────────────┐
           │               │               │
    ┌──────┴─────┐  ┌──────┴─────┐  ┌──────┴─────┐
    │  Usuarios  │  │  Productos │  │  Pedidos   │
    │   :8081    │  │   :8082    │  │   :8083    │
    └──────┬─────┘  └──────┬─────┘  └──────┬─────┘
           │               │               │
    ┌──────┴─────┐  ┌──────┴─────┐  ┌──────┴─────┐
    │  MySQL     │  │  MongoDB   │  │  PostgreSQL│
    └────────────┘  └────────────┘  └────────────┘
```

## Ventajas

- Escalabilidad independiente
- Equipos autónomos
- Despliegue continuo
- Aislamiento de fallos
- Flexibilidad tecnológica

## Desafíos

- Complejidad operacional
- Comunicación entre servicios
- Consistencia de datos
- Debugging distribuido
- Monitoreo

## Cuándo Usar Microservicios

**Sí usar si:**
- Equipo grande (>10 devs)
- Dominios de negocio claros
- Necesidad de escalar partes específicas
- Despliegues frecuentes e independientes

**No usar si:**
- Proyecto pequeño o MVP
- Equipo pequeño
- Dominio simple
- Sin experiencia en sistemas distribuidos

## Herramientas Recomendadas

- **Service Discovery**: Eureka, Consul
- **API Gateway**: Spring Cloud Gateway, Kong
- **Config**: Spring Cloud Config, Consul
- **Mensajería**: RabbitMQ, Kafka
- **Monitoreo**: Prometheus + Grafana, ELK Stack
- **Tracing**: Zipkin, Jaeger
