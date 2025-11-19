# AgroMarket - Proyecto de Aprendizaje

Proyecto educativo para aprender **Spring Boot + JPA + Angular**

## Estructura del Proyecto

```
proyecto1/
├── backend/                    # Spring Boot + JPA
│   ├── src/main/java/com/agromarket/
│   │   ├── controller/         # REST Controllers
│   │   ├── service/            # Business Logic
│   │   ├── repository/         # Data Access (JPA)
│   │   ├── model/              # Entities
│   │   └── config/             # Configuration
│   └── pom.xml                 # Maven dependencies
│
├── frontend/                   # Angular 17
│   ├── src/app/
│   │   ├── components/         # UI Components
│   │   ├── services/           # HTTP Services
│   │   └── models/             # TypeScript interfaces
│   └── package.json            # npm dependencies
│
├── index.html                  # Frontend original (HTML/CSS/JS)
└── productos.html              # Página de productos original
```

---

## Requisitos

### Backend
- **Java 17+** - [Descargar](https://adoptium.net/)
- **Maven 3.8+** - [Descargar](https://maven.apache.org/download.cgi)

### Frontend
- **Node.js 18+** - [Descargar](https://nodejs.org/)
- **npm** (viene con Node.js)

---

## Cómo Ejecutar

### 1. Backend (Spring Boot)

```bash
# Ir al directorio del backend
cd backend

# Compilar y ejecutar
./mvnw spring-boot:run

# O en Windows:
mvnw.cmd spring-boot:run
```

El backend estará disponible en:
- **API REST**: http://localhost:8080/api
- **H2 Console**: http://localhost:8080/h2-console

#### Probar la API con curl:

```bash
# Obtener todos los productos
curl http://localhost:8080/api/productos

# Obtener productos destacados
curl http://localhost:8080/api/productos/destacados

# Buscar productos
curl "http://localhost:8080/api/productos/buscar?nombre=manzana"

# Crear un producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Limón","precio":3.50,"stock":100}'
```

### 2. Frontend (Angular)

```bash
# Ir al directorio del frontend
cd frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
ng serve
# o
npm start
```

El frontend estará disponible en: http://localhost:4200

---

## Conceptos Clave para Estudiar

### Spring Boot

| Archivo | Concepto | Descripción |
|---------|----------|-------------|
| `AgroMarketApplication.java` | @SpringBootApplication | Punto de entrada |
| `ProductoController.java` | @RestController, @GetMapping | Endpoints REST |
| `ProductoService.java` | @Service, @Transactional | Lógica de negocio |
| `ProductoRepository.java` | JpaRepository | Acceso a datos |
| `Producto.java` | @Entity, @ManyToOne | Entidades JPA |
| `application.properties` | Configuración | DB, server, JPA |

### JPA/Hibernate

| Anotación | Uso |
|-----------|-----|
| `@Entity` | Marca clase como tabla |
| `@Id` | Clave primaria |
| `@GeneratedValue` | Auto-incremento |
| `@Column` | Configuración de columna |
| `@ManyToOne` | Relación muchos a uno |
| `@OneToMany` | Relación uno a muchos |
| `@Query` | Consultas JPQL personalizadas |

### Angular

| Archivo | Concepto | Descripción |
|---------|----------|-------------|
| `app.config.ts` | providers | Configuración global |
| `app.routes.ts` | Routes | Sistema de rutas |
| `producto.service.ts` | HttpClient, Observable | Peticiones HTTP |
| `producto-lista.component.ts` | @Component, ngOnInit | Componente UI |
| `carrito.service.ts` | BehaviorSubject | Estado reactivo |

### Patrones y Conceptos

1. **Inyección de Dependencias**: Constructor injection en Services
2. **Patrón Repository**: Separación de acceso a datos
3. **DTOs vs Entities**: Transferencia vs Persistencia
4. **Observables (RxJS)**: Programación reactiva
5. **Two-way binding**: `[(ngModel)]` en Angular

---

## Endpoints de la API

### Productos

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/productos` | Listar todos |
| GET | `/api/productos/{id}` | Obtener por ID |
| GET | `/api/productos/disponibles` | Solo disponibles |
| GET | `/api/productos/destacados` | Productos destacados |
| GET | `/api/productos/categoria/{id}` | Por categoría |
| GET | `/api/productos/buscar?nombre=x` | Buscar por nombre |
| GET | `/api/productos/filtrar?...` | Búsqueda avanzada |
| GET | `/api/productos/paginado?page=0&size=10` | Con paginación |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| PATCH | `/api/productos/{id}/stock?cantidad=5` | Actualizar stock |
| DELETE | `/api/productos/{id}` | Eliminar producto |

### Categorías

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/categorias` | Listar todas |
| GET | `/api/categorias/activas` | Solo activas |
| GET | `/api/categorias/{id}` | Obtener por ID |
| POST | `/api/categorias` | Crear categoría |
| PUT | `/api/categorias/{id}` | Actualizar |
| DELETE | `/api/categorias/{id}` | Eliminar |

---

## Acceder a la Base de Datos H2

1. Ir a http://localhost:8080/h2-console
2. Usar estos datos:
   - **JDBC URL**: `jdbc:h2:mem:agromarketdb`
   - **User**: `sa`
   - **Password**: (vacío)
3. Click en "Connect"

Consultas SQL de ejemplo:
```sql
SELECT * FROM productos;
SELECT * FROM categorias;
SELECT p.*, c.nombre as categoria
FROM productos p
JOIN categorias c ON p.categoria_id = c.id;
```

---

## Ejercicios Sugeridos

### Nivel Básico
1. Agregar un nuevo campo `origen` a Producto
2. Crear un endpoint para obtener productos por rango de precio
3. Agregar un componente Angular para mostrar estadísticas

### Nivel Intermedio
4. Implementar paginación en el frontend
5. Agregar validaciones con `@Valid` y mensajes de error
6. Crear un filtro por múltiples categorías

### Nivel Avanzado
7. Agregar autenticación con Spring Security
8. Implementar caché con Redis
9. Crear tests unitarios y de integración

---

## Próximos Pasos: Microservicios

Para convertir este proyecto en microservicios:

1. **Separar en servicios**:
   - `producto-service` (puerto 8081)
   - `categoria-service` (puerto 8082)
   - `carrito-service` (puerto 8083)

2. **Agregar Spring Cloud**:
   - Eureka Server (Service Discovery)
   - API Gateway
   - Config Server

3. **Comunicación entre servicios**:
   - REST con Feign Client
   - Mensajería con RabbitMQ/Kafka

---

## Recursos de Aprendizaje

- [Spring Boot Guides](https://spring.io/guides)
- [Angular Tutorial](https://angular.io/tutorial)
- [Baeldung JPA](https://www.baeldung.com/jpa-entities)
- [RxJS Learn](https://www.learnrxjs.io/)

---

## Troubleshooting

### El backend no inicia
- Verificar que Java 17+ esté instalado: `java -version`
- Puerto 8080 ocupado: cambiar en `application.properties`

### El frontend no conecta con el backend
- Verificar que el backend esté corriendo
- Revisar la URL en los services: `http://localhost:8080/api`
- Verificar configuración CORS en `WebConfig.java`

### Error "Cannot find module"
```bash
cd frontend
rm -rf node_modules
npm install
```

---

## Licencia

Proyecto educativo de libre uso para aprendizaje.
