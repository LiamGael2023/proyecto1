package com.agromarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa una categoría de productos
 *
 * CONCEPTOS JPA:
 * - @Entity: Marca la clase como una entidad JPA (se mapea a una tabla)
 * - @Table: Especifica el nombre de la tabla en la BD
 * - @Id: Marca el campo como clave primaria
 * - @GeneratedValue: Auto-genera el valor del ID
 * - @Column: Configura la columna en la BD
 * - @OneToMany: Relación uno a muchos (una categoría tiene muchos productos)
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    private String imagen;

    private Boolean activo = true;

    /**
     * Relación uno a muchos con Producto
     * - mappedBy: Indica que la relación está mapeada por el campo "categoria" en Producto
     * - cascade: Las operaciones se propagan a los productos
     * - orphanRemoval: Elimina productos huérfanos
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Categoria() {
    }

    // Constructor con parámetros
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    // Método helper para agregar productos
    public void addProducto(Producto producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}
