package com.ejemplo.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ========================================
 * ENTIDAD JPA - CATEGORIA
 * ========================================
 *
 * Esta entidad demuestra RELACIONES entre entidades.
 * En bases de datos relacionales, las tablas se conectan
 * mediante claves foráneas. JPA maneja estas relaciones
 * como asociaciones entre objetos.
 */
@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El nombre de categoría es obligatorio")
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    /**
     * ========================================
     * RELACIÓN ONE-TO-MANY
     * ========================================
     * Una categoría puede tener MUCHOS productos.
     *
     * mappedBy: indica que la relación es bidireccional y
     * el lado "dueño" está en Producto (campo 'categoria')
     *
     * cascade: operaciones en cascada
     * - PERSIST: al guardar categoría, guarda productos
     * - MERGE: al actualizar categoría, actualiza productos
     * - REMOVE: al eliminar categoría, elimina productos
     * - ALL: todas las operaciones
     *
     * fetch: estrategia de carga
     * - LAZY: carga los productos solo cuando se acceden (recomendado)
     * - EAGER: carga los productos inmediatamente
     *
     * orphanRemoval: elimina productos huérfanos
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductoConCategoria> productos = new ArrayList<>();

    // Método helper para mantener la relación bidireccional
    public void addProducto(ProductoConCategoria producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }

    public void removeProducto(ProductoConCategoria producto) {
        productos.remove(producto);
        producto.setCategoria(null);
    }
}
