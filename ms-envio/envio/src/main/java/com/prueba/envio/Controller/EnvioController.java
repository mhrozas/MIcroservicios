package com.prueba.envio.Controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.envio.Model.Envio;
import com.prueba.envio.Service.EnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/envio")
@Tag ( name = "envio", description = "sistema de envios")
public class EnvioController {
    @Autowired
    private EnvioService service;

    @PostMapping
     @Operation(summary = "Crea un nuevo envio", description = "Crea un envio desde cero")
    @ApiResponse(
        responseCode = "200", 
        description = "Envio creado")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudo crear el envio")
    public ResponseEntity<Envio> crear (@RequestBody Envio envio){
        return new ResponseEntity<>(service.save(envio), HttpStatus.CREATED);
    }

    @GetMapping
     @Operation(summary = "Obtener todos los envios", description = "Obtener todos los envios por id")
    @ApiResponse(
        responseCode = "200", 
        description = "Envios encontrados")
    @ApiResponse(
        responseCode = "404", 
        description = "No se puedo encontrar envios")
    public ResponseEntity <List<Envio>> listar(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
     @Operation(summary = "Buscar envio", description = "Buscar envio por id")
    @ApiResponse(
        responseCode = "200", 
        description = "Envio encontrado")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudo encontrar envios con esa id")
    public ResponseEntity<Map<String, Object>> obtenerCompleto(@PathVariable Integer id){
        Map<String, Object> respuesta = service.buscarEnvioCompleto(id);
        return respuesta.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(respuesta); 
    }

    @DeleteMapping("/{id}")
     @Operation(summary = "Eliminar envio", description = "Eliminar un envio por id")
    @ApiResponse(
        responseCode = "200", 
        description = "Envio eliminado exitosamente")
    @ApiResponse(
        responseCode = "404", 
        description = "No se encontro el envio")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
     @Operation(summary = "Buscar envio por estado", description = "Buscar un envio por el estado")
    @ApiResponse(
        responseCode = "200", 
        description = "Envio encontrado")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudo encontrar un envio con ese estado")
    public ResponseEntity<List<Envio>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.filtrarPorEstado(estado));
    }

    @PutMapping("/{id}")
     @Operation(summary = "Actualizar un envio", description = "Actualizar un envio por id")
    @ApiResponse(
        responseCode = "200", 
        description = "Envio actualizado")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudo actualizar el envio")
    public ResponseEntity<Envio> actualizar(@PathVariable Integer id, @RequestBody Envio envio) {
        envio.setId(id);
        return ResponseEntity.ok(service.save(envio));
    }

    @GetMapping("/pedido/{id}")
     @Operation(summary = "Buscar envio por pedido", description = "Buscar un envio por pedido id")
    @ApiResponse(
        responseCode = "200", 
        description = "Envio por id encontrado")
    @ApiResponse(
        responseCode = "404", 
        description = "No se encontro ningun pedido con esa id")
    public ResponseEntity<List<Envio>> buscarPorPedido(@PathVariable Integer id) {
    return ResponseEntity.ok(service.filtrarPorPedido(id));
}

}
