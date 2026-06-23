package com.prueba.inventario.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.inventario.Model.Inventario;
import com.prueba.inventario.Service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/inventario")
@Tag(name= "inventario", description = "Operaciones relacionadas con la gestion del Inventario")
//http://localhost:8082/doc/swagger-ui/index.html

public class InventarioController {
    @Autowired
    private InventarioService service;

    @PostMapping
    @Operation(summary = "Crea Inventario", description = "Crea un nuevo Inventario desde 0")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario creado con exito"),
        @ApiResponse(responseCode = "404", description = "Inventario no creado, revise el JSON")
    })
    public ResponseEntity<Inventario> crear (@RequestBody Inventario inventario){
        return new ResponseEntity<>(service.save(inventario), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "obtener todos los inventarios", description = "Obtiene una lista con todos los Inventarios")
    public ResponseEntity <List<Inventario>> listar(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "obtener Inventario por ID", description = "Obtiene un Inventario por ID")
    public ResponseEntity<Map<String, Object>> obtenerCompleto(@PathVariable Integer id){
        Map<String, Object> respuesta = service.buscarInventarioCompleto(id);
        return respuesta.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(respuesta); 
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un Inventario por ID", description = "Elimina un Inventario segun su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario eliminado con exito"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cantidad")
    @Operation(summary = "Actualiza la cantidad de un inventario", description = "Actualiza un la cantidad de un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario actualizado con exito"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
        })
    public ResponseEntity<Inventario> actualizarCantidad(@PathVariable Integer id, @PathVariable Integer valor) {
        return ResponseEntity.ok(service.actualizarCantidad(id, valor));
    }

    @GetMapping("/stock/{ropaId}")
    @Operation(summary = "Obtiene un el stock de un producto ", description = "Obtiene el stock de un producto segun su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario obtenido con exito"),
        @ApiResponse(responseCode = "404", description = "inventario no encontrado")
        })
    public ResponseEntity<Map<String, Object>> verificarStock(@PathVariable Integer ropaId) {
        return ResponseEntity.ok(service.verificarStock(ropaId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza el inventario por ID", description = "Actualiza el inventario segun su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario actualizado con exito"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
        })
    public ResponseEntity<Inventario> actualizar(
            @PathVariable Integer id, @RequestBody Inventario inventario) {
        inventario.setId(id);
        return ResponseEntity.ok(service.save(inventario));
    }


}
