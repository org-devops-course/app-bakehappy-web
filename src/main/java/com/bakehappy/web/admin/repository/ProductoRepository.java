package com.bakehappy.web.admin.repository;

import com.bakehappy.web.admin.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria_IdCategoria(Long idCategoria);

    List<Producto> findByStockGreaterThanOrderByStockDesc(int stock);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}
