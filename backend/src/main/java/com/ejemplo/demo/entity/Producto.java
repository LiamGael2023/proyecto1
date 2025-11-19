package com.ejemplo.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ========================================
 * ENTIDAD JPA - PRODUCTO
 * ========================================
 *
 * Una ENTIDAD en JPA representa una tabla en la base de datos.
 * Cada instancia de la entidad representa una fila en esa tabla.
 *
 * JPA (Java Persistence API) es una especificación que define
 * cómo mapear objetos Java a tablas de base de datos (ORM).
 * Hibernate es la implementación más popular de JPA.
 */
@Entity // Marca esta clase como una entidad JPA (tabla en BD)
@Table(name = "productos") // Nombre de la tabla (opcional, por defecto usa el nombre de la clase)
@Data // Lombok: genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Lombok: genera constructor con todos los argumentos
@Builder // Lombok: patrón builder para crear objetos
public class Producto {

    /**
     * ========================================
     * CLAVE PRIMARIA
     * ========================================
     */
    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento
    // Estrategias de generación:
    // - IDENTITY: Auto-incremento de la BD
    // - SEQUENCE: Usa secuencias de la BD
    // - TABLE: Usa una tabla auxiliar
    // - AUTO: Hibernate decide
    private Long id;

    /**
     * ========================================
     * CAMPOS BÁSICOS
     * ========================================
     */

    @Column(name = "nombre", nullable = false, length = 100)
    // @Column personaliza la columna:
    // - name: nombre en la BD
    // - nullable: si permite NULL
    // - length: longitud máxima (para String)
    // - unique: si debe ser único
    @NotBlank(message = "El nombre es obligatorio") // Validación
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Column(length = 500)
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    // precision: total de dígitos
    // scale: dígitos decimales
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Column(nullable = false)
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Column(name = "activo")
    private Boolean activo = true;

    /**
     * ========================================
     * CAMPOS DE AUDITORÍA
     * ========================================
     */

    @Column(name = "fecha_creacion", updatable = false)
    // updatable = false: no se puede modificar después de crear
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * ========================================
     * CALLBACKS DEL CICLO DE VIDA
     * ========================================
     * JPA proporciona métodos que se ejecutan automáticamente
     * en diferentes momentos del ciclo de vida de la entidad
     */

    @PrePersist // Se ejecuta ANTES de insertar en la BD
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }

    @PreUpdate // Se ejecuta ANTES de actualizar en la BD
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
