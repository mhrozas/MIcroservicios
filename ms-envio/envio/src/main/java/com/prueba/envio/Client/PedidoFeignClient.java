package com.prueba.envio.Client;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.prueba.envio.Model.DTO.PedidoDTO;
@FeignClient(name = "pedido", url = "localhost:8083")
public interface PedidoFeignClient {
    @GetMapping("/api/v1/pedidos/{id}")
        PedidoDTO obtenerPedidoPorId(@PathVariable("id") Integer id);

}
