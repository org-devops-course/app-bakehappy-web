package com.bakehappy.web.admin.repository;

import java.util.Optional;

import com.bakehappy.web.admin.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndClave(String email, String clave);

    long countByRol(String rol);
    long countByRolAndEstado(String rol, Usuario.Estado estado);
}

