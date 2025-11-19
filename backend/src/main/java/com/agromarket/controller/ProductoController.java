package com.agromarket.controller;

import com.agromarket.model.Producto;
import com.agromarket.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Productos
 *
 * Este controller demuestra:
 * - Endpoints CRUD completos
 * - Búsqueda y filtrado
 * - Paginación
 * - Múltiples parámetros opcionales
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // ========== ENDPOINTS GET ==========

    /**
     * GET /api/productos
     * Obtiene todos los productos
     */
    @GetMapping
    public ResponseEntity<List<Producto>> getAll() {
        return ResponseEntity.ok(productoService.findAll());
    }

    /**
     * GET /api/productos/disponibles
     * Obtiene solo productos disponibles
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Producto>> getDisponibles() {
        return ResponseEntity.ok(productoService.findDisponibles());
    }

    /**
     * GET /api/productos/destacados
     * Obtiene productos destacados
     */
    @GetMapping("/destacados")
    public ResponseEntity<List<Producto>> getDestacados() {
        return ResponseEntity.ok(productoService.findDestacados());
    }

    /**
     * GET /api/productos/{id}
     * Obtiene un producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/productos/categoria/{categoriaId}
     * Obtiene productos por categoría
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> getByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.findByCategoria(categoriaId));
    }

    /**
     * GET /api/productos/buscar?nombre=xxx
     * Busca productos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscar(nombre));
    }

    /**
     * GET /api/productos/filtrar?nombre=xxx&categoriaId=1&precioMax=100
     * Búsqueda avanzada con múltiples filtros opcionales
     * @RequestParam(required = false) hace el parámetro opcional
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<Producto>> filtrar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMax) {

        return ResponseEntity.ok(
                productoService.busquedaAvanzada(nombre, categoriaId, precioMax)
        );
    }

    /**
     * GET /api/productos/paginado?page=0&size=10&sortBy=precio&sortDir=asc
     * Obtiene productos con paginación
     */
    @GetMapping("/paginado")
    public ResponseEntity<Page<Producto>> getPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(
                productoService.findPaginated(page, size, sortBy, sortDir)
        );
    }

    // ========== ENDPOINTS POST ==========

    /**
     * POST /api/productos
     * Crea un nuevo producto
     */
    @PostMapping
    public ResponseEntity<Producto> create(@Valid @RequestBody Producto producto) {
        try {
            Producto nuevo = productoService.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== ENDPOINTS PUT ==========

    /**
     * PUT /api/productos/{id}
     * Actualiza un producto
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id,
                                           @Valid @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.update(id, producto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PATCH /api/productos/{id}/stock?cantidad=5
     * Actualiza el stock de un producto
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        try {
            Producto producto = productoService.actualizarStock(id, cantidad);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== ENDPOINTS DELETE ==========

    /**
     * DELETE /api/productos/{id}
     * Elimina un producto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
