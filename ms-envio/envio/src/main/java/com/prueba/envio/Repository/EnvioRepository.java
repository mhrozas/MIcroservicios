package com.prueba.envio.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.envio.Model.Envio;
public interface EnvioRepository  extends JpaRepository<Envio, Integer>{

    // Buscar envío por pedido
    List<Envio> findByPedidoId(Integer pedidoId);

    // Filtrar por estado
    List<Envio> findByEstado(String estado);
}



