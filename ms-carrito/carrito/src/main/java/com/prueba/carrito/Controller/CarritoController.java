package com.prueba.carrito.Controller;
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

import com.prueba.carrito.Model.Carrito;
import com.prueba.carrito.Service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/carrito")
@Tag ( name = "carrito", description = "sistema de carrito")
public class CarritoController {
 @Autowired
    private CarritoService service;

    @PostMapping
    @Operation(summary = "Crea un nuevo carrito", description = "Crea un carrito desde cero")
    @ApiResponse(
        responseCode = "200", 
        description = "Carrito creado")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudo crear carrito")
    public ResponseEntity<Carrito> crear (@RequestBody Carrito carrito){
        return new ResponseEntity<>(service.save(carrito), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los carritos", description = "Obtiene una lista de todos los carritos")
    @ApiResponse(
        responseCode = "200", 
        description = "Carritos encontrados")
    @ApiResponse(
        responseCode = "404", 
        description = "No se encontro carritos")
    public ResponseEntity <List<Carrito>> listar(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista de carritos", description = "Obtiene una lista de carritos por id")
    @ApiResponse(
        responseCode = "200", 
        description = "Carritos encontrados")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudieron encontrar carritos")
    public ResponseEntity<Map<String, Object>> obtenerCompleto(@PathVariable Integer id){
        Map<String, Object> respuesta = service.buscarCarritoCompleto(id);
        return respuesta.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(respuesta); 
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar carrito", description = "Eliminar carrito por id")
    @ApiResponse(
        responseCode = "200", 
        description = "Eliminado exitosamente")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudo eliminar el carrito")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar por usuario", description = "Obtiene un carrito por usuario id")
    @ApiResponse(
        responseCode = "200", 
        description = "Carrito encontrado")
    @ApiResponse(
        responseCode = "404", 
        description = "Usuario no tiene asignado un carrito")
    public ResponseEntity<List<Carrito>> listarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.findByUsuarioId(usuarioId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar carrito", description = "Actualizar carrito por id")
    @ApiResponse(
        responseCode = "200", 
        description = "Carrito actulizado")
    @ApiResponse(
        responseCode = "404", 
        description = "No se pudo actualizar carrito")
    public ResponseEntity<Carrito> actualizar(@PathVariable Integer id, @RequestBody Carrito carrito) {
        carrito.setId(id);
        return ResponseEntity.ok(service.save(carrito));
    }

}
