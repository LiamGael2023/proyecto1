package com.ejemplo.demo.service;

import com.ejemplo.demo.entity.Producto;
import com.ejemplo.demo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ========================================
 * CAPA DE SERVICIO - PRODUCTO
 * ========================================
 *
 * La capa de servicio contiene la LÓGICA DE NEGOCIO.
 * Es el intermediario entre el controlador y el repositorio.
 *
 * Responsabilidades:
 * - Validaciones de negocio
 * - Orquestación de operaciones
 * - Manejo de transacciones
 * - Transformaciones de datos
 *
 * @Service marca esta clase como un componente de servicio
 * Spring la detecta y crea un bean automáticamente
 *
 * @RequiredArgsConstructor (Lombok) genera un constructor
 * con todos los campos final, permitiendo inyección de dependencias
 */
@Service
@RequiredArgsConstructor // Inyección de dependencias por constructor
public class ProductoService {

    // Inyección de dependencias
    // Spring automáticamente inyecta la implementación del repositorio
    private final ProductoRepository productoRepository;

    /**
     * ========================================
     * OPERACIONES CRUD BÁSICAS
     * ========================================
     */

    /**
     * Obtener todos los productos
     */
    @Transactional(readOnly = true) // Optimiza para lecturas
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    /**
     * Obtener producto por ID
     */
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Crear nuevo producto
     *
     * @Transactional asegura que todas las operaciones en este método
     * se ejecuten en una transacción. Si algo falla, se hace rollback.
     */
    @Transactional
    public Producto crear(Producto producto) {
        // Validación de negocio
        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }
        return productoRepository.save(producto);
    }

    /**
     * Actualizar producto existente
     */
    @Transactional
    public Producto actualizar(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setStock(productoActualizado.getStock());
                    producto.setActivo(productoActualizado.getActivo());
                    // save() detecta que tiene ID y hace UPDATE en vez de INSERT
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    /**
     * Eliminar producto
     */
    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    /**
     * ========================================
     * OPERACIONES DE NEGOCIO
     * ========================================
     */

    /**
     * Buscar productos por nombre
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtener productos activos
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivoTrue();
    }

    /**
     * Buscar por rango de precio
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        return productoRepository.findByPrecioBetween(min, max);
    }

    /**
     * Obtener productos con stock bajo
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerConStockBajo(Integer stockMinimo) {
        return productoRepository.findProductosConStockBajo(stockMinimo);
    }

    /**
     * Actualizar stock de un producto
     */
    @Transactional
    public Producto actualizarStock(Long id, Integer nuevoStock) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setStock(nuevoStock);
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    /**
     * Desactivar producto
     */
    @Transactional
    public Producto desactivar(Long id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setActivo(false);
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    /**
     * Búsqueda con filtros múltiples
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarConFiltros(String nombre, BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.buscarConFiltros(nombre, precioMin, precioMax);
    }
}
