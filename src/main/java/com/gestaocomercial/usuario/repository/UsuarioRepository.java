package com.gestaocomercial.usuario.repository;

import com.gestaocomercial.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Long, Usuario> {
}
