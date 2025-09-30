package com.example.Proyecto_DAW.admin.repository;

import com.example.Proyecto_DAW.admin.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Puedes agregar métodos personalizados si lo necesitas
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    List<Cliente> findByFechaRegistroAfter(LocalDateTime fecha);
}