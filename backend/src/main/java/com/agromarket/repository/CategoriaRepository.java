package com.agromarket.repository;

import com.agromarket.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Categoria
 *
 * CONCEPTOS:
 * - JpaRepository<T, ID>: Proporciona métodos CRUD automáticamente
 *   - T: Tipo de entidad (Categoria)
 *   - ID: Tipo de la clave primaria (Long)
 *
 * - Métodos incluidos automáticamente:
 *   - save(), findById(), findAll(), deleteById(), count(), etc.
 *
 * - Query Methods: Spring Data JPA genera consultas basándose en el nombre del método
 *   - findByNombre -> SELECT * FROM categorias WHERE nombre = ?
 *   - findByActivoTrue -> SELECT * FROM categorias WHERE activo = true
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Query Method: Busca por nombre exacto
    Optional<Categoria> findByNombre(String nombre);

    // Query Method: Busca categorías activas
    List<Categoria> findByActivoTrue();

    // Query Method: Busca por nombre que contenga un texto (ignorando mayúsculas)
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    // Query Method: Verifica si existe una categoría con ese nombre
    boolean existsByNombre(String nombre);

    /**
     * @Query: Permite escribir consultas JPQL personalizadas
     * JPQL usa nombres de entidades y campos Java, no tablas SQL
     */
    @Query("SELECT c FROM Categoria c WHERE c.activo = true ORDER BY c.nombre")
    List<Categoria> findAllActivasOrdenadas();

    /**
     * Query nativa SQL (cuando necesitas SQL específico)
     */
    @Query(value = "SELECT * FROM categorias WHERE activo = true AND nombre LIKE %?1%",
           nativeQuery = true)
    List<Categoria> buscarPorNombreNativo(String nombre);
}
