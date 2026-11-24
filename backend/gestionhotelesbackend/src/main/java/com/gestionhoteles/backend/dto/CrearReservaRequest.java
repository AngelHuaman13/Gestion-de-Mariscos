package com.gestionhoteles.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CrearReservaRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 30) String telefono,
        @NotNull @Future LocalDateTime fechaHora,
        @NotNull @Min(1) @Max(20) Integer personas,
        @Size(max = 500) String comentario) {}
