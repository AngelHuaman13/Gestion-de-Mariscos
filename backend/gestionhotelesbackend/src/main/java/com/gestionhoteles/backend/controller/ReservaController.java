package com.gestionhoteles.backend.controller;

import com.gestionhoteles.backend.dto.CrearReservaRequest;
import com.gestionhoteles.backend.entity.Reserva;
import com.gestionhoteles.backend.repository.ReservaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaRepository reservas;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva crear(@Valid @RequestBody CrearReservaRequest request) {
        Reserva reserva = new Reserva();
        reserva.setNombre(request.nombre().trim());
        reserva.setTelefono(request.telefono().trim());
        reserva.setFechaHora(request.fechaHora());
        reserva.setPersonas(request.personas());
        reserva.setComentario(request.comentario());
        return reservas.save(reserva);
    }
}
