package com.ejemplo.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * ========================================
 * ENTIDAD CON RELACIÓN MANY-TO-ONE
 * ========================================
 *
 * Demuestra el lado "muchos" de una relación.
 * Muchos productos pertenecen a UNA categoría.
 */
@Entity
@Table(name = "productos_con_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoConCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(nullable = false)
    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;

    /**
     * ========================================
     * RELACIÓN MANY-TO-ONE
     * ========================================
     *
     * @ManyToOne: Muchos productos -> Una categoría
     *
     * @JoinColumn: Define la columna de clave foránea
     * - name: nombre de la columna FK en esta tabla
     * - referencedColumnName: columna referenciada (por defecto es la PK)
     *
     * fetch = LAZY es recomendado para evitar cargar datos innecesarios
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /**
     * ========================================
     * NOTA SOBRE RELACIONES BIDIRECCIONALES
     * ========================================
     *
     * En una relación bidireccional:
     * - El lado @ManyToOne es el "dueño" (tiene la FK)
     * - El lado @OneToMany usa mappedBy
     *
     * IMPORTANTE: Siempre mantén ambos lados sincronizados
     * usando métodos helper (ver Categoria.addProducto)
     */
}
