package com.agromarket.controller;

import com.agromarket.model.Categoria;
import com.agromarket.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Categorías
 *
 * CONCEPTOS REST:
 * - @RestController: Combina @Controller + @ResponseBody
 * - @RequestMapping: Define la URL base para todos los endpoints
 * - @GetMapping, @PostMapping, etc.: Mapean métodos HTTP
 * - @PathVariable: Extrae variables de la URL
 * - @RequestBody: Convierte JSON del body a objeto Java
 * - @RequestParam: Extrae parámetros de query string
 * - ResponseEntity: Permite controlar el código HTTP de respuesta
 *
 * MÉTODOS HTTP:
 * - GET: Obtener recursos
 * - POST: Crear recursos
 * - PUT: Actualizar recursos completamente
 * - PATCH: Actualizar parcialmente
 * - DELETE: Eliminar recursos
 */
@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*") // Permite CORS para desarrollo
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // ========== ENDPOINTS GET ==========

    /**
     * GET /api/categorias
     * Obtiene todas las categorías
     */
    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        List<Categoria> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }

    /**
     * GET /api/categorias/activas
     * Obtiene solo categorías activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> getActivas() {
        List<Categoria> categorias = categoriaService.findAllActivas();
        return ResponseEntity.ok(categorias);
    }

    /**
     * GET /api/categorias/{id}
     * Obtiene una categoría por ID
     * @PathVariable extrae el valor de {id} de la URL
     */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        return categoriaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/categorias/buscar?nombre=xxx
     * Busca categorías por nombre
     * @RequestParam extrae parámetros de la query string
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscar(@RequestParam String nombre) {
        List<Categoria> categorias = categoriaService.buscar(nombre);
        return ResponseEntity.ok(categorias);
    }

    // ========== ENDPOINTS POST ==========

    /**
     * POST /api/categorias
     * Crea una nueva categoría
     * @Valid activa las validaciones de la entidad
     * @RequestBody convierte el JSON del body a objeto Categoria
     */
    @PostMapping
    public ResponseEntity<Categoria> create(@Valid @RequestBody Categoria categoria) {
        try {
            Categoria nuevaCategoria = categoriaService.save(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== ENDPOINTS PUT ==========

    /**
     * PUT /api/categorias/{id}
     * Actualiza una categoría existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable Long id,
                                            @Valid @RequestBody Categoria categoria) {
        try {
            Categoria actualizada = categoriaService.update(id, categoria);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========== ENDPOINTS DELETE ==========

    /**
     * DELETE /api/categorias/{id}
     * Elimina una categoría
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            categoriaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PATCH /api/categorias/{id}/desactivar
     * Desactiva una categoría (soft delete)
     */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Categoria> desactivar(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.desactivar(id);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
