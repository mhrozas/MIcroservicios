package com.prueba.carrito.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.prueba.carrito.Model.DTO.RopaDTO;

@FeignClient(name = "ropa", url = "localhost:8082")
public interface RopaFeignClient {
    @GetMapping("/api/v1/ropa/{id}")
    RopaDTO obtenerRopaPorId(@PathVariable("id") Integer id);
}


