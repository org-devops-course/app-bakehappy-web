package com.example.Proyecto_DAW.admin.repository;

import com.example.Proyecto_DAW.admin.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdCliente(Long idCliente);
}