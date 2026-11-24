package com.gestionhoteles.backend.repository;

import com.gestionhoteles.backend.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {}
