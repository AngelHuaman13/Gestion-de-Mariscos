package com.gestionhoteles.backend.controller;

import org.springframework.web.server.ResponseStatusException;
import com.gestionhoteles.backend.entity.Producto;
import com.gestionhoteles.backend.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;

    private void exigirAdmin(HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute("admin"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión como administrador.");
        }
    }

    @GetMapping
    public List<Producto> listar() { return service.listar(); }

    @GetMapping("/buscar")
    public List<Producto> buscar(@RequestParam(defaultValue = "") String nombre) { return service.buscar(nombre); }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Integer id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody Producto p, HttpSession session) {
        exigirAdmin(session);
        return service.crear(p);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Integer id, @Valid @RequestBody Producto p, HttpSession session) {
        exigirAdmin(session);
        return service.actualizar(id, p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Integer id, HttpSession session) {
        exigirAdmin(session);
        service.desactivar(id);
    }
}