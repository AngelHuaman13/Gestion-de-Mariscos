package com.gestionhoteles.backend.config;

import com.gestionhoteles.backend.entity.Producto;
import com.gestionhoteles.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final ProductoRepository productos;

    @Bean
    CommandLineRunner cargarProductos() {
        return args -> {
            if (productos.count() == 0) {
                productos.save(crearProducto("Langosta Entera", "Langosta del Mar de Grau, viva o cocida.", "Fresco", "Viva / Cocida", "Mar de Grau", "120.00", 10, "langosta.png"));
                productos.save(crearProducto("Ceviche Mixto", "Pescado y mariscos frescos del día.", "Fresco", "Porción individual", "Pesca del día", "38.00", 30, "ceviche.png"));
                productos.save(crearProducto("Camarones Jumbo", "Camarones jumbo seleccionados.", "Congelado", "Por kilo", "Piura", "65.00", 20, "camarones.png"));
                productos.save(crearProducto("Pulpo al Olivo", "Pulpo tierno en crema de aceituna.", "Fresco", "Plato preparado", "Pesca del día", "42.00", 15, "pulpo.png"));
            }
        };
    }

    private Producto crearProducto(String nombre, String descripcion, String tipo, String presentacion,
                                   String origen, String precio, Integer stock, String imagen) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setTipo(tipo);
        producto.setPresentacion(presentacion);
        producto.setOrigen(origen);
        producto.setPrecio(new BigDecimal(precio));
        producto.setStock(stock);
        producto.setImagen(imagen);
        producto.setActivo(true);
        return producto;
    }
}
