package com.prueba.carrito.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.carrito.Client.RopaFeignClient;
import com.prueba.carrito.Client.UsuarioFeignClient;
import com.prueba.carrito.Model.Carrito;
import com.prueba.carrito.Model.DTO.RopaDTO;
import com.prueba.carrito.Repository.CarritoRepository;

@Service
public class CarritoService {
 @Autowired
    private CarritoRepository repository;

    @Autowired
    private RopaFeignClient ropaClient;

    @Autowired
    private UsuarioFeignClient usuarioClient;

    public Carrito save(Carrito carrito) {
        return repository.save(carrito);
    }

    public List<Carrito> findAll() {
        return repository.findAll();
    }

    public Map<String, Object> buscarCarritoCompleto(Integer id) {
    Carrito carrito = repository.findById(id).orElse(null);
    Map<String, Object> respuesta = new HashMap<>();
    if (carrito != null) {
        List<RopaDTO> ropas = carrito.getRopaIds().stream()
            .map(ropaClient::obtenerRopaPorId)
            .collect(Collectors.toList());
        respuesta.put("id", carrito.getId());
        respuesta.put("total_articulos", carrito.getTotal_articulos());
        respuesta.put("total", carrito.getTotal());
        respuesta.put("usuarioId", carrito.getUsuarioId());
        respuesta.put("ropas", ropas);
        try {
            respuesta.put("usuario", usuarioClient.obtenerUsuarioId(carrito.getUsuarioId()));
        } catch (Exception e) {
            respuesta.put("usuario", "No disponible");
        }
    }
        return respuesta;
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Carrito> findByUsuarioId(Integer carritoId) {
        return repository.findByUsuarioId(carritoId);
    }

}
