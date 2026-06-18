package com.prueba.envio.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.envio.Client.PedidoFeignClient;
import com.prueba.envio.Model.DTO.PedidoDTO;
import com.prueba.envio.Model.Envio;
import com.prueba.envio.Repository.EnvioRepository;

@Service
public class EnvioService {
    @Autowired
    private EnvioRepository repository;

    @Autowired
    private PedidoFeignClient pedidoClient;  


    public Envio save(Envio envio) {
        return repository.save(envio);
    }

    public List<Envio> findAll() {
        return repository.findAll();
    }

    public Map<String, Object> buscarEnvioCompleto(Integer id) {
    Envio envio = repository.findById(id).orElse(null);
    Map<String, Object> respuesta = new HashMap<>();
    if (envio != null) {
        respuesta.put("id", envio.getId());
        respuesta.put("pedidoId", envio.getPedidoId());
        respuesta.put("direccion", envio.getDireccion());
        respuesta.put("estado", envio.getEstado());
        respuesta.put("trackingCode", envio.getTrackingCode());
        respuesta.put("fechaCreacion", envio.getFechaCreacion());
        try {
            PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(envio.getPedidoId());
            respuesta.put("pedido", pedido);
        } catch (Exception e) {
            respuesta.put("pedido", "No disponible");
        }
    }
    return respuesta;
}


    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Envio> filtrarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public List<Envio> filtrarPorPedido(Integer pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }


}
