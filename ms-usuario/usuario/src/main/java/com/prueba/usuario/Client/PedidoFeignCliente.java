package com.prueba.usuario.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.prueba.usuario.Model.DTO.PedidoDTO;

@FeignClient(name = "pedido", url = "localhost:8083")
public interface PedidoFeignCliente {
    @GetMapping("/api/v1/pedidos/{id}")
    PedidoDTO obtenerPedidoPorId(@PathVariable("id") Integer id);
}

