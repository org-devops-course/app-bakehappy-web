package com.example.Proyecto_DAW.admin.repository;

import java.util.Optional;

import com.example.Proyecto_DAW.admin.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndClave(String email, String clave);

    long countByRol(String rol);
    long countByRolAndEstado(String rol, Usuario.Estado estado);
}

