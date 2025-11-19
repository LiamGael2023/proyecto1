package com.ejemplo.demo.repository;

import com.ejemplo.demo.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * ========================================
 * REPOSITORIO JPA - CATEGORIA
 * ========================================
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar por nombre
    Optional<Categoria> findByNombre(String nombre);

    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    // Verificar si existe
    boolean existsByNombre(String nombre);

    // Buscar categorías que contengan texto en nombre o descripción
    @Query("SELECT c FROM Categoria c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Categoria> buscarPorTexto(String texto);

    // Obtener categorías ordenadas por nombre
    List<Categoria> findAllByOrderByNombreAsc();
}
