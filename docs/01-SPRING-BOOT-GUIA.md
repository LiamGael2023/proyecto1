# Guía de Aprendizaje: Spring Boot + JPA

## Introducción

Spring Boot es un framework que simplifica el desarrollo de aplicaciones Java basadas en Spring. Proporciona:
- Configuración automática
- Servidor web embebido
- Gestión de dependencias simplificada
- Métricas y monitoreo

## Arquitectura en Capas

```
┌─────────────────────────────────────┐
│         CLIENTE (Angular)           │
└─────────────────┬───────────────────┘
                  │ HTTP/REST
┌─────────────────┴───────────────────┐
│      CONTROLLER (Controlador)       │
│   - Recibe peticiones HTTP          │
│   - Valida entrada                  │
│   - Retorna respuestas              │
└─────────────────┬───────────────────┘
                  │
┌─────────────────┴───────────────────┐
│        SERVICE (Servicio)           │
│   - Lógica de negocio               │
│   - Validaciones                    │
│   - Transacciones                   │
└─────────────────┬───────────────────┘
                  │
┌─────────────────┴───────────────────┐
│      REPOSITORY (Repositorio)       │
│   - Acceso a datos                  │
│   - Consultas a BD                  │
└─────────────────┬───────────────────┘
                  │
┌─────────────────┴───────────────────┐
│         DATABASE (H2/MySQL)         │
└─────────────────────────────────────┘
```

## Conceptos Clave

### 1. Inyección de Dependencias (DI)

Spring gestiona la creación y conexión de objetos automáticamente:

```java
@Service
@RequiredArgsConstructor
public class ProductoService {
    // Spring inyecta el repositorio automáticamente
    private final ProductoRepository repository;
}
```

### 2. Anotaciones Principales

| Anotación | Descripción |
|-----------|-------------|
| `@SpringBootApplication` | Punto de entrada de la app |
| `@RestController` | Controlador REST |
| `@Service` | Componente de servicio |
| `@Repository` | Componente de acceso a datos |
| `@Entity` | Entidad JPA (tabla) |
| `@Autowired` | Inyección de dependencias |

### 3. JPA (Java Persistence API)

JPA permite mapear objetos Java a tablas de BD:

```java
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
}
```

### 4. Spring Data JPA Repositories

Interfaces que Spring implementa automáticamente:

```java
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Spring crea la implementación basándose en el nombre del método
    List<Producto> findByNombreContaining(String texto);
}
```

## Flujo de una Petición

1. **Cliente** envía petición HTTP
2. **Controller** recibe y valida
3. **Service** ejecuta lógica de negocio
4. **Repository** accede a la BD
5. **Respuesta** regresa al cliente

## Ejecución del Proyecto

```bash
cd backend
./mvnw spring-boot:run
```

La API estará en: `http://localhost:8080`
Consola H2: `http://localhost:8080/h2-console`

## Endpoints de Ejemplo

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/productos` | Listar todos |
| GET | `/api/productos/{id}` | Obtener uno |
| POST | `/api/productos` | Crear |
| PUT | `/api/productos/{id}` | Actualizar |
| DELETE | `/api/productos/{id}` | Eliminar |
