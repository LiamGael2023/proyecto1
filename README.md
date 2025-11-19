# Proyecto de Aprendizaje: Spring Boot + JPA + Angular

Este proyecto contiene ejemplos prácticos y documentación para aprender desarrollo full-stack con Java y Angular.

## Estructura del Proyecto

```
proyecto1/
├── backend/                    # API REST con Spring Boot
│   ├── src/main/java/
│   │   └── com/ejemplo/demo/
│   │       ├── controller/     # Controladores REST
│   │       ├── service/        # Lógica de negocio
│   │       ├── repository/     # Acceso a datos (JPA)
│   │       ├── entity/         # Entidades/Modelos
│   │       └── config/         # Configuración
│   └── src/main/resources/
│       └── application.properties
├── frontend/                   # Aplicación Angular
│   └── src/app/
│       ├── components/         # Componentes UI
│       ├── services/           # Servicios HTTP
│       └── models/             # Interfaces TypeScript
└── docs/                       # Documentación
    ├── 01-SPRING-BOOT-GUIA.md
    ├── 02-MICROSERVICIOS.md
    └── 03-ANGULAR-GUIA.md
```

## Tecnologías

### Backend
- **Java 17**
- **Spring Boot 3.2**
- **Spring Data JPA**
- **H2 Database** (en memoria)
- **Lombok**
- **Maven**

### Frontend
- **Angular 17+**
- **TypeScript**
- **RxJS**

## Comenzar

### Requisitos
- Java 17+
- Maven 3.6+
- Node.js 18+ (para Angular)
- Angular CLI (`npm install -g @angular/cli`)

### Backend

```bash
cd backend

# Ejecutar
./mvnw spring-boot:run

# O en Windows
mvnw.cmd spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

### Consola H2

Accede a la base de datos en: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Password: (vacío)

### Frontend

```bash
cd frontend

# Instalar dependencias
npm install

# Ejecutar
ng serve
```

La aplicación estará en: `http://localhost:4200`

## Endpoints API

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/productos` | Listar todos |
| GET | `/api/productos/{id}` | Obtener por ID |
| POST | `/api/productos` | Crear nuevo |
| PUT | `/api/productos/{id}` | Actualizar |
| DELETE | `/api/productos/{id}` | Eliminar |
| GET | `/api/productos/buscar?nombre=x` | Buscar por nombre |
| GET | `/api/productos/activos` | Solo activos |
| GET | `/api/productos/precio?min=x&max=y` | Por rango de precio |

## Ejemplo de Petición

### Crear Producto
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop",
    "descripcion": "Laptop gaming",
    "precio": 1299.99,
    "stock": 10
  }'
```

### Listar Productos
```bash
curl http://localhost:8080/api/productos
```

## Documentación

Consulta la carpeta `docs/` para guías detalladas:

1. **01-SPRING-BOOT-GUIA.md** - Fundamentos de Spring Boot y JPA
2. **02-MICROSERVICIOS.md** - Arquitectura de microservicios
3. **03-ANGULAR-GUIA.md** - Fundamentos de Angular

## Temas Cubiertos

### Spring Boot
- Arquitectura en capas (Controller, Service, Repository)
- Entidades JPA y mapeo objeto-relacional
- Query Methods y JPQL
- Validaciones
- Manejo de excepciones
- Transacciones

### JPA/Hibernate
- Anotaciones (@Entity, @Table, @Column, etc.)
- Relaciones (OneToMany, ManyToOne)
- Estrategias de generación de ID
- Callbacks del ciclo de vida

### Microservicios
- Diferencias con monolitos
- Componentes (Gateway, Discovery, Config)
- Comunicación síncrona y asíncrona
- Patrones de resiliencia

### Angular
- Componentes y módulos
- Servicios e inyección de dependencias
- Data binding
- HTTP Client y Observables
- Routing
- Formularios

## Próximos Pasos

1. Ejecuta el backend y prueba los endpoints
2. Lee la documentación en `docs/`
3. Modifica el código y experimenta
4. Añade nuevas funcionalidades

## Recursos Adicionales

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Angular Docs](https://angular.io/docs)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
