package com.gestionhoteles.backend.repository;
import com.gestionhoteles.backend.entity.Pedido; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface PedidoRepository extends JpaRepository<Pedido,Integer> { List<Pedido> findByEstado(String estado); List<Pedido> findAllByOrderByFechaPedidoDesc(); }
