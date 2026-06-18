package com.prueba.carrito.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.carrito.Model.Carrito;
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    List<Carrito> findByUsuarioId(Integer usuarioId);
}

