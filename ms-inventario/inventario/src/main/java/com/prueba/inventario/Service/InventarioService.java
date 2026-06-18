package com.prueba.inventario.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.inventario.Client.RopaFeignClient;
import com.prueba.inventario.Model.DTO.RopaDTO;
import com.prueba.inventario.Model.Inventario;
import com.prueba.inventario.Repository.InventarioRepository;

@Service
public class InventarioService {
    @Autowired
    private InventarioRepository repository;

    @Autowired
    private RopaFeignClient ropaClient;

    public Inventario save(Inventario inventario) {
        return repository.save(inventario);
    }

    public List<Inventario> findAll() {
        return repository.findAll();
    }
    
    //Muestra el inventario completo de un tipo de ropa
    public Map<String, Object> buscarInventarioCompleto(Integer id) {
    Inventario inventario = repository.findById(id).orElse(null);
    Map<String, Object> respuesta = new HashMap<>();
    if (inventario != null) {
        RopaDTO ropa = ropaClient.obtenerRopaPorId(inventario.getRopaId());
        respuesta.put("id", inventario.getId());
        respuesta.put("nombre", inventario.getNombre());
        respuesta.put("detalle", inventario.getDetalle());
        respuesta.put("cantidad", inventario.getCantidad());
        respuesta.put("fechaCreacion", inventario.getFechaCreacion());
        respuesta.put("ropa", ropa);
    }
        return respuesta;
    }

    //Eliminar inventario
    public void delete(Integer id) {
        repository.deleteById(id);
    }
    
    // Actualizar solo la cantidad (sin reemplazar todo el registro)
    public Inventario actualizarCantidad(Integer id, Integer nuevaCantidad) {
        Inventario inv = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado id: " + id));
        inv.setCantidad(nuevaCantidad);
        return repository.save(inv);
    }

    // Verificar si hay stock disponible para un producto
    public Map<String, Object> verificarStock(Integer ropaId) {
        List<Inventario> inventarios = repository.findByRopaId(ropaId);
        if (inventarios == null || inventarios.isEmpty()) {
            throw new RuntimeException("Producto no encontrado en inventario");
        }
        Inventario inv = inventarios.get(0);
        Map<String, Object> res = new HashMap<>();
        res.put("ropaId", ropaId);
        res.put("cantidad", inv.getCantidad());
        res.put("disponible", inv.getCantidad() > 0);
        return res;
    }

    // Listar productos con stock bajo
    public List<Inventario> listarStockBajo(Integer bajo) {
        return repository.findByCantidadLessThan(bajo);
    }

}
