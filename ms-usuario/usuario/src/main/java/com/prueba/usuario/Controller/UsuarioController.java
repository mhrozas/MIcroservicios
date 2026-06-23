package com.prueba.usuario.Controller;

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

import com.prueba.usuario.Model.Usuario;
import com.prueba.usuario.Service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/usuario")
@Tag(name= "usuarios", description = "Operaciones relacionadas con la gestion de usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @PostMapping
    @Operation(summary = "Crea un usuario", description = "Crea un usuario desde 0")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado con exito"),
        @ApiResponse(responseCode = "404", description = "Usuario no creado, revise el JSON")
    })
    public ResponseEntity<Usuario> crear (@RequestBody Usuario usuario){
        return new ResponseEntity<>(service.save(usuario), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    public ResponseEntity <List<Usuario>> listar(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "obtener usuario por ID", description = "Obtiene un usuario por ID")
    public ResponseEntity<Map<String, Object>> obtenerCompleto(@PathVariable Integer id){
        Map<String, Object> respuesta = service.buscarUsuarioCompleto(id);
        return respuesta.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(respuesta); 
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un usuario por ID", description = "Elimina un usuario segun su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado con exito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza usuario por ID", description = "Actualiza un usuario segun su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado con exito"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
    return ResponseEntity.ok(service.update(id, usuario));
    }

    @GetMapping("/buscar/{email}")
    @Operation(summary = "obtener usuario por su correo", description = "Obtiene un usuario segun su correo")
    public ResponseEntity<Map<String, Object>> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }


}
