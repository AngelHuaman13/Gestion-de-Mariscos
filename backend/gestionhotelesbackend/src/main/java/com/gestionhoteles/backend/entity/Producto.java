package com.gestionhoteles.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @NotBlank
    @Pattern(regexp = "Fresco|Congelado")
    private String tipo;

    @Size(max = 50)
    private String presentacion;

    @Size(max = 100)
    private String origen;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    private String imagen;

    @NotNull
    private Boolean activo = true;
}