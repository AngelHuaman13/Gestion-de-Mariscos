package com.gestionhoteles.backend.repository;
import com.gestionhoteles.backend.entity.Producto; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ProductoRepository extends JpaRepository<Producto,Integer> { List<Producto> findByActivoTrue(); List<Producto> findByNombreContainingIgnoreCase(String nombre); }
