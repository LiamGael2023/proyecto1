package com.ejemplo.demo.controller;

import com.ejemplo.demo.entity.Producto;
import com.ejemplo.demo.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * ========================================
 * CONTROLADOR REST - PRODUCTO
 * ========================================
 *
 * Los controladores manejan las peticiones HTTP y devuelven respuestas.
 * Son el punto de entrada de la API REST.
 *
 * @RestController combina:
 * - @Controller: marca como controlador MVC
 * - @ResponseBody: indica que los métodos retornan datos (no vistas)
 *
 * @RequestMapping define la ruta base para todos los endpoints
 *
 * @CrossOrigin permite peticiones desde otros dominios (CORS)
 * Necesario para que el frontend Angular pueda consumir la API
 */
@RestController
@RequestMapping("/api/productos") // Ruta base: /api/productos
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permitir peticiones de cualquier origen
public class ProductoController {

    private final ProductoService productoService;

    /**
     * ========================================
     * ENDPOINTS CRUD
     * ========================================
     *
     * RESTful API sigue convenciones:
     * - GET: Obtener recursos
     * - POST: Crear recursos
     * - PUT: Actualizar recursos completos
     * - PATCH: Actualizar parcialmente
     * - DELETE: Eliminar recursos
     */

    /**
     * GET /api/productos
     * Obtener todos los productos
     *
     * ResponseEntity permite controlar:
     * - Código de estado HTTP
     * - Headers
     * - Body de la respuesta
     */
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos); // 200 OK
    }

    /**
     * GET /api/productos/{id}
     * Obtener producto por ID
     *
     * @PathVariable extrae el valor del path
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok) // 200 OK si existe
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    /**
     * POST /api/productos
     * Crear nuevo producto
     *
     * @RequestBody convierte el JSON del body a objeto Java
     * @Valid activa las validaciones de la entidad
     */
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto nuevo = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // 201 Created
    }

    /**
     * PUT /api/productos/{id}
     * Actualizar producto existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/productos/{id}
     * Eliminar producto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ========================================
     * ENDPOINTS DE BÚSQUEDA
     * ========================================
     *
     * @RequestParam extrae parámetros de la query string
     * Ejemplo: /api/productos/buscar?nombre=laptop
     */

    /**
     * GET /api/productos/buscar?nombre=texto
     * Buscar productos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(
            @RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    /**
     * GET /api/productos/activos
     * Obtener solo productos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> obtenerActivos() {
        return ResponseEntity.ok(productoService.obtenerActivos());
    }

    /**
     * GET /api/productos/precio?min=10&max=100
     * Buscar por rango de precio
     */
    @GetMapping("/precio")
    public ResponseEntity<List<Producto>> buscarPorPrecio(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(productoService.buscarPorRangoPrecio(min, max));
    }

    /**
     * GET /api/productos/stock-bajo?minimo=10
     * Obtener productos con stock bajo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> obtenerStockBajo(
            @RequestParam(defaultValue = "10") Integer minimo) {
        return ResponseEntity.ok(productoService.obtenerConStockBajo(minimo));
    }

    /**
     * GET /api/productos/filtrar?nombre=x&precioMin=y&precioMax=z
     * Búsqueda con múltiples filtros opcionales
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<Producto>> filtrar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax) {
        return ResponseEntity.ok(productoService.buscarConFiltros(nombre, precioMin, precioMax));
    }

    /**
     * ========================================
     * ENDPOINTS DE ACCIONES
     * ========================================
     */

    /**
     * PATCH /api/productos/{id}/stock
     * Actualizar solo el stock
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        try {
            return ResponseEntity.ok(productoService.actualizarStock(id, stock));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PATCH /api/productos/{id}/desactivar
     * Desactivar un producto
     */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Producto> desactivar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productoService.desactivar(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
