package com.agromarket.service;

import com.agromarket.model.Categoria;
import com.agromarket.model.Producto;
import com.agromarket.repository.CategoriaRepository;
import com.agromarket.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de Productos
 */
@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository,
                          CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // ========== OPERACIONES DE LECTURA ==========

    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> findDisponibles() {
        return productoRepository.findByDisponibleTrue();
    }

    @Transactional(readOnly = true)
    public List<Producto> findDestacados() {
        return productoRepository.findByDestacadoTrueAndDisponibleTrue();
    }

    @Transactional(readOnly = true)
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscar(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Búsqueda avanzada con múltiples filtros
     */
    @Transactional(readOnly = true)
    public List<Producto> busquedaAvanzada(String nombre, Long categoriaId, Double precioMax) {
        return productoRepository.busquedaAvanzada(nombre, categoriaId, precioMax);
    }

    /**
     * Obtiene productos paginados
     *
     * PAGINACIÓN:
     * - page: Número de página (empieza en 0)
     * - size: Cantidad de elementos por página
     * - sort: Campo y dirección de ordenamiento
     */
    @Transactional(readOnly = true)
    public Page<Producto> findPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productoRepository.findByDisponibleTrue(pageable);
    }

    // ========== OPERACIONES DE ESCRITURA ==========

    /**
     * Guarda un nuevo producto
     */
    public Producto save(Producto producto) {
        // Validar que la categoría exista
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoría no encontrada: " + producto.getCategoria().getId()));
            producto.setCategoria(categoria);
        }

        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente
     */
    public Producto update(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setStock(productoActualizado.getStock());
                    producto.setImagen(productoActualizado.getImagen());
                    producto.setUnidad(productoActualizado.getUnidad());
                    producto.setDisponible(productoActualizado.getDisponible());
                    producto.setDestacado(productoActualizado.getDestacado());

                    // Actualizar categoría si se proporciona
                    if (productoActualizado.getCategoria() != null &&
                        productoActualizado.getCategoria().getId() != null) {
                        Categoria categoria = categoriaRepository
                                .findById(productoActualizado.getCategoria().getId())
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                        producto.setCategoria(categoria);
                    }

                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    /**
     * Elimina un producto
     */
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    /**
     * Actualiza el stock de un producto
     */
    public Producto actualizarStock(Long id, Integer cantidad) {
        return productoRepository.findById(id)
                .map(producto -> {
                    int nuevoStock = producto.getStock() + cantidad;
                    if (nuevoStock < 0) {
                        throw new RuntimeException("Stock insuficiente");
                    }
                    producto.setStock(nuevoStock);

                    // Si el stock llega a 0, marcar como no disponible
                    if (nuevoStock == 0) {
                        producto.setDisponible(false);
                    }

                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    /**
     * Marca todos los productos sin stock como agotados
     */
    public int marcarAgotados() {
        return productoRepository.marcarAgotados();
    }
}
