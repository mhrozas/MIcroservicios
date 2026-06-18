package com.prueba.inventario.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.inventario.Model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    List<Inventario> findByRopaId(Integer ropaId);

    List<Inventario> findByCantidadLessThan(Integer cantidad);
}

