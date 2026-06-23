package com.prueba.carrito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prueba.carrito.Client.RopaFeignClient;
import com.prueba.carrito.Client.UsuarioFeignClient;
import com.prueba.carrito.Model.Carrito;
import com.prueba.carrito.Model.DTO.RopaDTO;
import com.prueba.carrito.Model.DTO.UsuarioDTO;
import com.prueba.carrito.Repository.CarritoRepository;
import com.prueba.carrito.Service.CarritoService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {

    @InjectMocks
    private CarritoService carritoService;

    @Mock
    private CarritoRepository repository;

    @Mock
    private RopaFeignClient ropaClient;

    @Mock
    private UsuarioFeignClient usuarioClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Given
        Carrito carrito = new Carrito();
        when(repository.save(carrito)).thenReturn(carrito);

        // When
        Carrito saved = carritoService.save(carrito);

        // Then
        assertNotNull(saved);
        verify(repository, times(1)).save(carrito);
    }

    @Test
    public void testFindAll() {
        // Given
        Carrito carrito = new Carrito();
        when(repository.findAll()).thenReturn(List.of(carrito));

        // When
        List<Carrito> carritos = carritoService.findAll();

        // Then
        assertNotNull(carritos);
        assertEquals(1, carritos.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testBuscarCarritoCompleto_Encontrado() {
        // Given
        Integer id = 1;
        Carrito carrito = new Carrito();
        carrito.setId(id);
        carrito.setUsuarioId(10);
        carrito.setTotal_articulos(2);
        carrito.setTotal(45000.0);
        carrito.setRopaIds(List.of(5, 6));

        RopaDTO ropa1 = new RopaDTO();
        RopaDTO ropa2 = new RopaDTO();
        UsuarioDTO usuario = new UsuarioDTO();

        when(repository.findById(id)).thenReturn(Optional.of(carrito));
        when(ropaClient.obtenerRopaPorId(5)).thenReturn(ropa1);
        when(ropaClient.obtenerRopaPorId(6)).thenReturn(ropa2);
        when(usuarioClient.obtenerUsuarioId(10)).thenReturn(usuario);

        // When
        Map<String, Object> respuesta = carritoService.buscarCarritoCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertEquals(id, respuesta.get("id"));
        assertEquals(2, respuesta.get("total_articulos"));
        assertEquals(45000.0, respuesta.get("total"));
        assertEquals(usuario, respuesta.get("usuario"));

        @SuppressWarnings("unchecked")
        List<RopaDTO> ropas = (List<RopaDTO>) respuesta.get("ropas");
        assertEquals(2, ropas.size());

        verify(repository, times(1)).findById(id);
        verify(ropaClient, times(1)).obtenerRopaPorId(5);
        verify(ropaClient, times(1)).obtenerRopaPorId(6);
    }

    @Test
    public void testBuscarCarritoCompleto_NoExiste() {
        // Given
        Integer id = 99;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Map<String, Object> respuesta = carritoService.buscarCarritoCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertTrue(respuesta.isEmpty());
        verify(repository, times(1)).findById(id);
        verify(ropaClient, never()).obtenerRopaPorId(any());
    }

    @Test
    public void testBuscarCarritoCompleto_UsuarioNoDisponible() {
        // Given
        Integer id = 1;
        Carrito carrito = new Carrito();
        carrito.setId(id);
        carrito.setUsuarioId(10);
        carrito.setRopaIds(List.of());

        when(repository.findById(id)).thenReturn(Optional.of(carrito));
        when(usuarioClient.obtenerUsuarioId(10)).thenThrow(new RuntimeException("Servicio no disponible"));

        // When
        Map<String, Object> respuesta = carritoService.buscarCarritoCompleto(id);

        // Then
        assertEquals("No disponible", respuesta.get("usuario"));
        verify(usuarioClient, times(1)).obtenerUsuarioId(10);
    }

    @Test
    public void testDelete() {
        // Given
        Integer id = 1;
        doNothing().when(repository).deleteById(id);

        // When
        carritoService.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testFindByUsuarioId() {
        // Given
        Integer usuarioId = 10;
        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);
        when(repository.findByUsuarioId(usuarioId)).thenReturn(List.of(carrito));

        // When
        List<Carrito> resultado = carritoService.findByUsuarioId(usuarioId);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByUsuarioId(usuarioId);
    }
}
