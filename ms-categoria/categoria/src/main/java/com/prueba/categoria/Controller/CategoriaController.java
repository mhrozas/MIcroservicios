package com.prueba.categoria.Controller;
import java.util.List;

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

import com.prueba.categoria.Model.Categoria;
import com.prueba.categoria.Service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController 
@RequestMapping("/api/v1/categoria")
@Tag(name= "categoria", description = "Operaciones relacionadas con categorias")
//http://localhost:8082/doc/swagger-ui/index.html
public class CategoriaController {
    @Autowired private CategoriaService service;

    @PostMapping
    @Operation (summary = "Crear categoria", description = "Crea una categoria desde 0")
    @ApiResponse(responseCode = "200", description = "Categoria creada exitosamente")
    @ApiResponse(responseCode = "404", description = "No se pudo crear la categoria")
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        return new ResponseEntity<>(service.save(categoria), HttpStatus.CREATED);
    }
    @GetMapping
    @Operation (summary = "Lista de todas las categorias", description = "Obtiene todas las categorias creadas")
    @ApiResponse(responseCode = "200", description = "Categorias")
    @ApiResponse(responseCode = "404", description = "No se encontro ninguna categoria")
    public ResponseEntity<List<Categoria>> listar() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    @Operation (summary = "Buscar categoria por id", description = "Busca la categoria por la id")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada")
    @ApiResponse(responseCode = "404", description = "No se pudo encontrar la categoria")
    public ResponseEntity<Categoria> obtener(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation (summary = "Actualizar categoria", description = "Actualizar categoria por id ")
    @ApiResponse(responseCode = "200", description = "Categoria actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "No se pudo actualizar la categoria")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok(service.update(id, categoria));
    }
    @DeleteMapping("/{id}")
    @Operation (summary = "Eliminar categoria", description = "Elimina categoria por id")
    @ApiResponse(responseCode = "200", description = "Categoria eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "No se pudo eliminar la categoria")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
