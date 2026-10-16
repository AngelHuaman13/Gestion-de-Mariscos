package com.gestionhoteles.backend.service;
import com.gestionhoteles.backend.entity.Producto; import com.gestionhoteles.backend.exception.ResourceNotFoundException; import com.gestionhoteles.backend.repository.ProductoRepository; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import java.util.*;
@Service @RequiredArgsConstructor public class ProductoService {
 private final ProductoRepository repo;
 public List<Producto> listar(){return repo.findByActivoTrue();}
 public List<Producto> buscar(String nombre){return repo.findByNombreContainingIgnoreCase(nombre).stream().filter(p->Boolean.TRUE.equals(p.getActivo())).toList();}
 public Producto obtener(Integer id){return repo.findById(id).filter(p->Boolean.TRUE.equals(p.getActivo())).orElseThrow(()->new ResourceNotFoundException("Producto no encontrado: "+id));}
 public Producto crear(Producto p){p.setIdProducto(null); if(p.getActivo()==null)p.setActivo(true);return repo.save(p);}
 public Producto actualizar(Integer id,Producto c){Producto p=obtener(id);p.setNombre(c.getNombre());p.setDescripcion(c.getDescripcion());p.setTipo(c.getTipo());p.setPresentacion(c.getPresentacion());p.setOrigen(c.getOrigen());p.setPrecio(c.getPrecio());p.setStock(c.getStock());p.setImagen(c.getImagen());return repo.save(p);}
 public void desactivar(Integer id){Producto p=obtener(id);p.setActivo(false);repo.save(p);}
}
