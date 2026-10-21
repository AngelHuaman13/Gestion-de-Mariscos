package com.gestionhoteles.backend.controller;
import com.gestionhoteles.backend.dto.CrearPedidoRequest; import com.gestionhoteles.backend.entity.Pedido; import com.gestionhoteles.backend.service.PedidoService; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/pedidos") @RequiredArgsConstructor public class PedidoController {
 private final PedidoService service;
 @GetMapping public List<Pedido> listar(){return service.listar();}
 @GetMapping("/{id}") public Pedido obtener(@PathVariable Integer id){return service.obtener(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Pedido crear(@Valid @RequestBody CrearPedidoRequest req){return service.crear(req);}
 @PatchMapping("/{id}/estado") public Pedido cambiarEstado(@PathVariable Integer id,@RequestBody Map<String,String> body){return service.cambiarEstado(id,body.getOrDefault("estado",""));}
}
