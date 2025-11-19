package com.ejemplo.demo.repository;

import com.ejemplo.demo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ========================================
 * REPOSITORIO JPA - PRODUCTO
 * ========================================
 *
 * Spring Data JPA proporciona una forma declarativa de crear
 * repositorios. Solo defines una interfaz y Spring crea la
 * implementación automáticamente.
 *
 * JpaRepository<Entidad, TipoId> proporciona métodos CRUD:
 * - save(entity): Guardar o actualizar
 * - findById(id): Buscar por ID
 * - findAll(): Obtener todos
 * - deleteById(id): Eliminar por ID
 * - count(): Contar registros
 * - existsById(id): Verificar existencia
 */
@Repository // Marca esta interfaz como un componente de repositorio
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * ========================================
     * QUERY METHODS (Métodos de Consulta)
     * ========================================
     *
     * Spring Data JPA crea consultas automáticamente basándose
     * en el nombre del método. Sigue convenciones específicas.
     */

    // Buscar por nombre exacto
    // SELECT * FROM productos WHERE nombre = ?
    Optional<Producto> findByNombre(String nombre);

    // Buscar por nombre conteniendo texto (LIKE %texto%)
    // SELECT * FROM productos WHERE nombre LIKE '%texto%'
    List<Producto> findByNombreContainingIgnoreCase(String texto);

    // Buscar productos activos
    // SELECT * FROM productos WHERE activo = true
    List<Producto> findByActivoTrue();

    // Buscar por rango de precio
    // SELECT * FROM productos WHERE precio BETWEEN ? AND ?
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    // Buscar productos con stock mayor a X
    // SELECT * FROM productos WHERE stock > ?
    List<Producto> findByStockGreaterThan(Integer stock);

    // Buscar productos con stock menor o igual a X (para alertas)
    List<Producto> findByStockLessThanEqual(Integer stock);

    // Buscar activos ordenados por precio ascendente
    List<Producto> findByActivoTrueOrderByPrecioAsc();

    // Combinar condiciones con And/Or
    List<Producto> findByActivoTrueAndStockGreaterThan(Integer stock);

    // Verificar existencia
    boolean existsByNombre(String nombre);

    // Contar productos activos
    long countByActivoTrue();

    /**
     * ========================================
     * JPQL (Java Persistence Query Language)
     * ========================================
     *
     * Cuando los query methods no son suficientes,
     * puedes escribir consultas JPQL personalizadas.
     *
     * JPQL usa nombres de entidades y campos (no tablas/columnas)
     */

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.precio < :precioMax")
    List<Producto> findProductosActivosBaratos(@Param("precioMax") BigDecimal precioMax);

    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);

    // Consulta con múltiples condiciones
    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:precioMin IS NULL OR p.precio >= :precioMin) AND " +
           "(:precioMax IS NULL OR p.precio <= :precioMax) AND " +
           "p.activo = true")
    List<Producto> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );

    /**
     * ========================================
     * NATIVE QUERIES (Consultas SQL Nativas)
     * ========================================
     *
     * Para casos especiales, puedes usar SQL nativo.
     * Usa nombres reales de tablas y columnas.
     */

    @Query(value = "SELECT * FROM productos WHERE stock = 0 AND activo = true",
           nativeQuery = true)
    List<Producto> findProductosSinStock();

    /**
     * ========================================
     * MODIFYING QUERIES (Consultas de Modificación)
     * ========================================
     *
     * Para UPDATE y DELETE masivos.
     * Requieren @Modifying y @Transactional
     */

    @org.springframework.data.jpa.repository.Modifying
    @Query("UPDATE Producto p SET p.activo = false WHERE p.stock = 0")
    int desactivarProductosSinStock();
}
