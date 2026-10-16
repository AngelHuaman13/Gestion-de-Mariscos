package com.gestionhoteles.backend.service;
import com.gestionhoteles.backend.entity.MensajeContacto; import com.gestionhoteles.backend.repository.MensajeContactoRepository; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor public class MensajeContactoService { private final MensajeContactoRepository repo; public MensajeContacto guardar(MensajeContacto m){m.setIdMensaje(null);return repo.save(m);} }
