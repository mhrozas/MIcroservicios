package com.prueba.usuario.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.usuario.Model.Usuario;
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByEmail(String email);
}

