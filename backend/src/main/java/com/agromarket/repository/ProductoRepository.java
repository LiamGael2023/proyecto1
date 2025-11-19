package com.agromarket.repository;

import com.agromarket.model.Categoria;
import com.agromarket.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Producto
 *
 * Este repository demuestra diferentes tipos de consultas:
 * 1. Query Methods automáticos
 * 2. Consultas JPQL con @Query
 * 3. Consultas con parámetros nombrados
 * 4. Consultas de modificación con @Modifying
 * 5. Paginación con Pageable
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // ========== QUERY METHODS BÁSICOS ==========

    // Busca productos por categoría
    List<Producto> findByCategoria(Categoria categoria);

    // Busca por ID de categoría
    List<Producto> findByCategoriaId(Long categoriaId);

    // Busca productos disponibles
    List<Producto> findByDisponibleTrue();

    // Busca productos destacados y disponibles
    List<Producto> findByDestacadoTrueAndDisponibleTrue();

    // Busca por nombre que contenga texto
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // ========== CONSULTAS CON RANGOS ==========

    // Productos con precio menor a X
    List<Producto> findByPrecioLessThan(Double precio);

    // Productos con precio entre X e Y
    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);

    // Productos con stock mayor a X
    List<Producto> findByStockGreaterThan(Integer stock);

    // ========== CONSULTAS JPQL PERSONALIZADAS ==========

    /**
     * Busca productos por categoría y rango de precio
     * @Param: Nombra los parámetros para mayor claridad
     */
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId " +
           "AND p.precio BETWEEN :precioMin AND :precioMax " +
           "AND p.disponible = true")
    List<Producto> findByCategoriaAndPrecioRange(
            @Param("categoriaId") Long categoriaId,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax
    );

    /**
     * Búsqueda avanzada por múltiples criterios
     */
    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:precioMax IS NULL OR p.precio <= :precioMax) " +
           "AND p.disponible = true")
    List<Producto> busquedaAvanzada(
            @Param("nombre") String nombre,
            @Param("categoriaId") Long categoriaId,
            @Param("precioMax") Double precioMax
    );

    /**
     * Cuenta productos por categoría
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria.id = :categoriaId")
    Long countByCategoria(@Param("categoriaId") Long categoriaId);

    // ========== PAGINACIÓN ==========

    /**
     * Encuentra todos los productos con paginación
     * Pageable permite: número de página, tamaño, ordenamiento
     */
    Page<Producto> findByDisponibleTrue(Pageable pageable);

    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);

    // ========== CONSULTAS DE MODIFICACIÓN ==========

    /**
     * @Modifying: Indica que esta query modifica datos
     * Requiere @Transactional en el servicio que la llame
     */
    @Modifying
    @Query("UPDATE Producto p SET p.disponible = false WHERE p.stock = 0")
    int marcarAgotados();

    @Modifying
    @Query("UPDATE Producto p SET p.precio = p.precio * :factor WHERE p.categoria.id = :categoriaId")
    int actualizarPreciosPorCategoria(
            @Param("categoriaId") Long categoriaId,
            @Param("factor") Double factor
    );
}
