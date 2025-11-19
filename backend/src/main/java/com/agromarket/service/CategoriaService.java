package com.agromarket.service;

import com.agromarket.model.Categoria;
import com.agromarket.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de Categorías
 *
 * CONCEPTOS:
 * - @Service: Marca la clase como un servicio de Spring (componente de lógica de negocio)
 * - @Autowired: Inyección de dependencias automática
 * - @Transactional: Gestión de transacciones de base de datos
 *
 * CAPAS DE LA APLICACIÓN:
 * Controller -> Service -> Repository -> Database
 *
 * El Service contiene:
 * - Validaciones de negocio
 * - Transformaciones de datos
 * - Orquestación de múltiples repositorios
 * - Manejo de transacciones
 */
@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Inyección de dependencias por constructor
     * (Preferido sobre @Autowired en campos)
     */
    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // ========== OPERACIONES DE LECTURA ==========

    /**
     * Obtiene todas las categorías
     * @Transactional(readOnly = true): Optimización para operaciones de solo lectura
     */
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    /**
     * Obtiene solo las categorías activas
     */
    @Transactional(readOnly = true)
    public List<Categoria> findAllActivas() {
        return categoriaRepository.findByActivoTrue();
    }

    /**
     * Busca una categoría por ID
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Busca una categoría por nombre
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    /**
     * Busca categorías que contengan el texto
     */
    @Transactional(readOnly = true)
    public List<Categoria> buscar(String texto) {
        return categoriaRepository.findByNombreContainingIgnoreCase(texto);
    }

    // ========== OPERACIONES DE ESCRITURA ==========

    /**
     * Guarda una nueva categoría
     * Incluye validación de negocio
     */
    public Categoria save(Categoria categoria) {
        // Validación: El nombre no debe existir ya
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }

    /**
     * Actualiza una categoría existente
     */
    public Categoria update(Long id, Categoria categoriaActualizada) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    // Solo actualiza si el nuevo nombre no existe (o es el mismo)
                    if (!categoria.getNombre().equals(categoriaActualizada.getNombre()) &&
                        categoriaRepository.existsByNombre(categoriaActualizada.getNombre())) {
                        throw new RuntimeException("Ya existe una categoría con el nombre: " +
                                categoriaActualizada.getNombre());
                    }

                    categoria.setNombre(categoriaActualizada.getNombre());
                    categoria.setDescripcion(categoriaActualizada.getDescripcion());
                    categoria.setImagen(categoriaActualizada.getImagen());
                    categoria.setActivo(categoriaActualizada.getActivo());

                    return categoriaRepository.save(categoria);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    /**
     * Elimina una categoría por ID
     */
    public void delete(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    /**
     * Desactiva una categoría (soft delete)
     * Preferible a eliminar para mantener historial
     */
    public Categoria desactivar(Long id) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setActivo(false);
                    return categoriaRepository.save(categoria);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }
}
