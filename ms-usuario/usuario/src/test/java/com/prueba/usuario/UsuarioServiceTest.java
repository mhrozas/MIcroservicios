package com.prueba.usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prueba.usuario.Model.Usuario;
import com.prueba.usuario.Repository.UsuarioRepository;
import com.prueba.usuario.Service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository repository;

    @Test
    public void testSave() {
        // Given
        Usuario usuario = new Usuario();
        when(repository.save(usuario)).thenReturn(usuario);

        // When
        Usuario saved = usuarioService.save(usuario);

        // Then
        assertNotNull(saved);
        verify(repository, times(1)).save(usuario);
    }

    @Test
    public void testFindAll() {
        // Given
        Usuario usuario = new Usuario();
        when(repository.findAll()).thenReturn(List.of(usuario));

        // When
        List<Usuario> usuarios = usuarioService.findAll();

        // Then
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testBuscarUsuarioCompleto() {
        // Given
        Integer usuarioId = 1;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@correo.com");

        when(repository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // When
        Map<String, Object> respuesta = usuarioService.buscarUsuarioCompleto(usuarioId);

        // Then
        assertNotNull(respuesta);
        assertEquals(usuarioId, respuesta.get("id"));
        assertEquals("Juan Pérez", respuesta.get("nombre"));
        assertEquals("juan@correo.com", respuesta.get("email"));

        verify(repository, times(1)).findById(usuarioId);
    }

    @Test
    public void testBuscarUsuarioCompleto_NoExiste() {
        // Given
        Integer usuarioId = 99;
        when(repository.findById(usuarioId)).thenReturn(Optional.empty());

        // When
        Map<String, Object> respuesta = usuarioService.buscarUsuarioCompleto(usuarioId);

        // Then
        assertNotNull(respuesta);
        assertTrue(respuesta.isEmpty());
        verify(repository, times(1)).findById(usuarioId);
    }

    @Test
    public void testDelete() {
        // Given
        Integer id = 1;
        doNothing().when(repository).deleteById(id);

        // When
        usuarioService.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdate_Exitoso() {
        // Given
        Integer id = 1;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);

        Usuario usuarioNuevosDatos = new Usuario();
        usuarioNuevosDatos.setNombre("Pedro Actualizado");

        when(repository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(repository.save(usuarioNuevosDatos)).thenReturn(usuarioNuevosDatos);

        // When
        Usuario result = usuarioService.update(id, usuarioNuevosDatos);

        // Then
        assertNotNull(result);
        assertEquals(id, usuarioNuevosDatos.getId());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(usuarioNuevosDatos);
    }

    @Test
    public void testUpdate_LanzaExcepcionCuandoNoExiste() {
        // Given
        Integer id = 99;
        Usuario usuarioNuevosDatos = new Usuario();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.update(id, usuarioNuevosDatos);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    public void testBuscarPorEmail() {
        // Given
        String email = "maria@correo.com";
        Usuario usuario = new Usuario();
        usuario.setId(2);
        usuario.setNombre("Maria");
        usuario.setEmail(email);
        usuario.setTelefono(5551234);

        when(repository.findByEmail(email)).thenReturn(List.of(usuario));

        // When
        Map<String, Object> respuesta = usuarioService.buscarPorEmail(email);

        // Then
        assertNotNull(respuesta);
        assertFalse(respuesta.isEmpty());
        assertEquals(2, respuesta.get("id"));
        assertEquals("Maria", respuesta.get("nombre"));
        assertEquals(email, respuesta.get("email"));
        assertEquals(5551234, respuesta.get("telefono"));

        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    public void testBuscarPorEmail_Vacio() {
        // Given
        String email = "noexiste@correo.com";
        when(repository.findByEmail(email)).thenReturn(List.of());

        // When
        Map<String, Object> respuesta = usuarioService.buscarPorEmail(email);

        // Then
        assertNotNull(respuesta);
        assertTrue(respuesta.isEmpty());
    }
}
