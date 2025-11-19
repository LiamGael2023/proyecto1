package com.agromarket.config;

import com.agromarket.model.Categoria;
import com.agromarket.model.Producto;
import com.agromarket.repository.CategoriaRepository;
import com.agromarket.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos de prueba
 *
 * CommandLineRunner se ejecuta después de que Spring Boot inicia
 * Útil para cargar datos iniciales en desarrollo
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public DataInitializer(CategoriaRepository categoriaRepository,
                          ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la BD está vacía
        if (categoriaRepository.count() == 0) {
            cargarDatos();
        }
    }

    private void cargarDatos() {
        System.out.println("Cargando datos de prueba...");

        // Crear categorías
        Categoria frutas = new Categoria("Frutas", "Frutas frescas de temporada");
        frutas.setImagen("https://images.unsplash.com/photo-1619566636858-adf3ef46400b?w=400");

        Categoria verduras = new Categoria("Verduras", "Verduras orgánicas");
        verduras.setImagen("https://images.unsplash.com/photo-1540420773420-3366772f4999?w=400");

        Categoria tuberculos = new Categoria("Tubérculos", "Tubérculos andinos");
        tuberculos.setImagen("https://images.unsplash.com/photo-1518977676601-b53f82ber95c?w=400");

        Categoria granos = new Categoria("Granos", "Granos y cereales");
        granos.setImagen("https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400");

        categoriaRepository.save(frutas);
        categoriaRepository.save(verduras);
        categoriaRepository.save(tuberculos);
        categoriaRepository.save(granos);

        // Crear productos - Frutas
        Producto manzana = new Producto("Manzana Delicia", 4.50, 100);
        manzana.setDescripcion("Manzanas rojas de Huaral, dulces y crocantes");
        manzana.setCategoria(frutas);
        manzana.setDestacado(true);
        manzana.setImagen("https://images.unsplash.com/photo-1619546813926-a78fa6372cd2?w=400");

        Producto platano = new Producto("Plátano de Seda", 3.00, 150);
        platano.setDescripcion("Plátanos orgánicos de la selva");
        platano.setCategoria(frutas);
        platano.setImagen("https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400");

        Producto naranja = new Producto("Naranja Valencia", 3.50, 80);
        naranja.setDescripcion("Naranjas jugosas para jugo");
        naranja.setCategoria(frutas);
        naranja.setImagen("https://images.unsplash.com/photo-1547514701-42782101795e?w=400");

        // Crear productos - Verduras
        Producto tomate = new Producto("Tomate Italiano", 5.00, 60);
        tomate.setDescripcion("Tomates maduros perfectos para ensaladas");
        tomate.setCategoria(verduras);
        tomate.setDestacado(true);
        tomate.setImagen("https://images.unsplash.com/photo-1546470427-e0ed4715edcc?w=400");

        Producto lechuga = new Producto("Lechuga Americana", 2.50, 40);
        lechuga.setDescripcion("Lechuga fresca y crocante");
        lechuga.setCategoria(verduras);
        lechuga.setImagen("https://images.unsplash.com/photo-1622206151226-18ca2c9ab4a1?w=400");

        Producto zanahoria = new Producto("Zanahoria", 3.00, 90);
        zanahoria.setDescripcion("Zanahorias frescas ricas en vitamina A");
        zanahoria.setCategoria(verduras);
        zanahoria.setImagen("https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?w=400");

        // Crear productos - Tubérculos
        Producto papa = new Producto("Papa Amarilla", 4.00, 200);
        papa.setDescripcion("Papa amarilla perfecta para causa y puré");
        papa.setCategoria(tuberculos);
        papa.setDestacado(true);
        papa.setImagen("https://images.unsplash.com/photo-1518977676601-b53f82ber95c?w=400");

        Producto camote = new Producto("Camote Morado", 3.50, 120);
        camote.setDescripcion("Camote dulce peruano");
        camote.setCategoria(tuberculos);
        camote.setImagen("https://images.unsplash.com/photo-1596097635121-14b63b7a0c19?w=400");

        // Crear productos - Granos
        Producto quinua = new Producto("Quinua Blanca", 12.00, 50);
        quinua.setDescripcion("Quinua orgánica de Puno");
        quinua.setCategoria(granos);
        quinua.setDestacado(true);
        quinua.setImagen("https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400");

        Producto arroz = new Producto("Arroz Extra", 4.50, 300);
        arroz.setDescripcion("Arroz graneado de primera calidad");
        arroz.setCategoria(granos);
        arroz.setImagen("https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400");

        // Guardar todos los productos
        productoRepository.save(manzana);
        productoRepository.save(platano);
        productoRepository.save(naranja);
        productoRepository.save(tomate);
        productoRepository.save(lechuga);
        productoRepository.save(zanahoria);
        productoRepository.save(papa);
        productoRepository.save(camote);
        productoRepository.save(quinua);
        productoRepository.save(arroz);

        System.out.println("Datos de prueba cargados exitosamente!");
        System.out.println("- " + categoriaRepository.count() + " categorías");
        System.out.println("- " + productoRepository.count() + " productos");
    }
}
