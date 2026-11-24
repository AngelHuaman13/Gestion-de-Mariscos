package com.gestionhoteles.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String telefono;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @NotNull
    @Min(1)
    @Max(20)
    @Column(nullable = false)
    private Integer personas;

    @Size(max = 500)
    @Column(length = 500)
    private String comentario;

    @Column(nullable = false, length = 40)
    private String estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @PrePersist
    void antesDeGuardar() {
        if (estado == null) estado = "Pendiente de confirmación";
        if (fechaSolicitud == null) fechaSolicitud = LocalDateTime.now();
    }
}
