package com.gestionhoteles.backend.service;
import com.gestionhoteles.backend.dto.CrearPedidoRequest; import com.gestionhoteles.backend.entity.*; import com.gestionhoteles.backend.exception.*; import com.gestionhoteles.backend.repository.*; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.math.BigDecimal; import java.util.*;
@Service @RequiredArgsConstructor public class PedidoService {
 private final PedidoRepository pedidos; private final ProductoRepository productos;
 public List<Pedido> listar(){return pedidos.findAllByOrderByFechaPedidoDesc();}
 public Pedido obtener(Integer id){return pedidos.findById(id).orElseThrow(()->new ResourceNotFoundException("Pedido no encontrado: "+id));}
 @Transactional public Pedido crear(CrearPedidoRequest req){
  Pedido pedido=new Pedido(); pedido.setNombreCliente(req.nombreCliente());pedido.setTelefono(req.telefono());pedido.setEmail(req.email());pedido.setDireccion(req.direccion());pedido.setTipoEntrega(req.tipoEntrega());pedido.setEstado("Pendiente");pedido.setTotal(BigDecimal.ZERO);
  BigDecimal total=BigDecimal.ZERO; List<ItemPedido> items=new ArrayList<>();
  for(var solicitud:req.items()) { Producto p=productos.findById(solicitud.idProducto()).filter(x->Boolean.TRUE.equals(x.getActivo())).orElseThrow(()->new ResourceNotFoundException("Producto no disponible: "+solicitud.idProducto())); if(p.getStock()<solicitud.cantidad())throw new BadRequestException("Stock insuficiente para "+p.getNombre()+". Disponible: "+p.getStock()); BigDecimal subtotal=p.getPrecio().multiply(BigDecimal.valueOf(solicitud.cantidad())); ItemPedido item=new ItemPedido();item.setPedido(pedido);item.setProducto(p);item.setCantidad(solicitud.cantidad());item.setPrecioUnitario(p.getPrecio());item.setSubtotal(subtotal);items.add(item);total=total.add(subtotal);p.setStock(p.getStock()-solicitud.cantidad());productos.save(p); }
  pedido.setItems(items);pedido.setTotal(total);return pedidos.save(pedido);
 }
 @Transactional public Pedido cambiarEstado(Integer id,String estado){if(!List.of("Pendiente","Preparando","Entregado","Cancelado").contains(estado))throw new BadRequestException("Estado inválido");Pedido p=obtener(id);p.setEstado(estado);return pedidos.save(p);}
}
