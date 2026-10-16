package com.gestionhoteles.backend.controller;
import com.gestionhoteles.backend.entity.Producto; import com.gestionhoteles.backend.service.ProductoService; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/productos") @RequiredArgsConstructor public class ProductoController {
 private final ProductoService service;
 @GetMapping public List<Producto> listar(){return service.listar();}
 @GetMapping("/buscar") public List<Producto> buscar(@RequestParam(defaultValue="") String nombre){return service.buscar(nombre);}
 @GetMapping("/{id}") public Producto obtener(@PathVariable Integer id){return service.obtener(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Producto crear(@Valid @RequestBody Producto p){return service.crear(p);}
 @PutMapping("/{id}") public Producto actualizar(@PathVariable Integer id,@Valid @RequestBody Producto p){return service.actualizar(id,p);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void desactivar(@PathVariable Integer id){service.desactivar(id);}
}
