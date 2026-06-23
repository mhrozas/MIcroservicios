package com.prueba.inventario;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prueba.inventario.Client.RopaFeignClient;
import com.prueba.inventario.Model.DTO.RopaDTO;
import com.prueba.inventario.Model.Inventario;
import com.prueba.inventario.Repository.InventarioRepository;
import com.prueba.inventario.Service.InventarioService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @InjectMocks
    private InventarioService inventarioService;

    @Mock
    private InventarioRepository repository;

    @Mock
    private RopaFeignClient ropaClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Given
        Inventario inventario = new Inventario();
        when(repository.save(inventario)).thenReturn(inventario);

        // When
        Inventario saved = inventarioService.save(inventario);

        // Then
        assertNotNull(saved);
        verify(repository, times(1)).save(inventario);
    }

    @Test
    public void testFindAll() {
        // Given
        Inventario inventario = new Inventario();
        when(repository.findAll()).thenReturn(List.of(inventario));

        // When
        List<Inventario> inventarios = inventarioService.findAll();

        // Then
        assertNotNull(inventarios);
        assertEquals(1, inventarios.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testBuscarInventarioCompleto_Encontrado() {
        // Given
        Integer id = 1;
        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setRopaId(5);
        inventario.setNombre("Camisa de lino");
        inventario.setDetalle("Stock principal");
        inventario.setCantidad(30);
        inventario.setFechaCreacion(new Date());

        RopaDTO ropa = new RopaDTO();

        when(repository.findById(id)).thenReturn(Optional.of(inventario));
        when(ropaClient.obtenerRopaPorId(5)).thenReturn(ropa);

        // When
        Map<String, Object> respuesta = inventarioService.buscarInventarioCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertEquals(id, respuesta.get("id"));
        assertEquals("Camisa de lino", respuesta.get("nombre"));
        assertEquals("Stock principal", respuesta.get("detalle"));
        assertEquals(30, respuesta.get("cantidad"));
        assertEquals(ropa, respuesta.get("ropa"));

        verify(repository, times(1)).findById(id);
        verify(ropaClient, times(1)).obtenerRopaPorId(5);
    }

    @Test
    public void testBuscarInventarioCompleto_NoExiste() {
        // Given
        Integer id = 99;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Map<String, Object> respuesta = inventarioService.buscarInventarioCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertTrue(respuesta.isEmpty());
        verify(repository, times(1)).findById(id);
        verifyNoInteractions(ropaClient);
    }

    @Test
    public void testDelete() {
        // Given
        Integer id = 1;
        doNothing().when(repository).deleteById(id);

        // When
        inventarioService.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testActualizarCantidad_Exitoso() {
        // Given
        Integer id = 1;
        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setCantidad(10);

        when(repository.findById(id)).thenReturn(Optional.of(inventario));
        when(repository.save(inventario)).thenReturn(inventario);

        // When
        Inventario actualizado = inventarioService.actualizarCantidad(id, 25);

        // Then
        assertNotNull(actualizado);
        assertEquals(25, actualizado.getCantidad());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(inventario);
    }

    @Test
    public void testActualizarCantidad_LanzaExcepcionCuandoNoExiste() {
        // Given
        Integer id = 99;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.actualizarCantidad(id, 10);
        });

        assertEquals("Inventario no encontrado id: " + id, exception.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testVerificarStock_Disponible() {
        // Given
        Integer ropaId = 5;
        Inventario inventario = new Inventario();
        inventario.setRopaId(ropaId);
        inventario.setCantidad(15);

        when(repository.findByRopaId(ropaId)).thenReturn(List.of(inventario));

        // When
        Map<String, Object> resultado = inventarioService.verificarStock(ropaId);

        // Then
        assertEquals(ropaId, resultado.get("ropaId"));
        assertEquals(15, resultado.get("cantidad"));
        assertEquals(true, resultado.get("disponible"));
        verify(repository, times(1)).findByRopaId(ropaId);
    }

    @Test
    public void testVerificarStock_LanzaExcepcionCuandoNoExiste() {
        // Given
        Integer ropaId = 99;
        when(repository.findByRopaId(ropaId)).thenReturn(List.of());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.verificarStock(ropaId);
        });

        assertEquals("Producto no encontrado en inventario", exception.getMessage());
        verify(repository, times(1)).findByRopaId(ropaId);
    }

    @Test
    public void testListarStockBajo() {
        // Given
        Integer umbral = 5;
        Inventario inventario = new Inventario();
        inventario.setCantidad(2);

        when(repository.findByCantidadLessThan(umbral)).thenReturn(List.of(inventario));

        // When
        List<Inventario> resultado = inventarioService.listarStockBajo(umbral);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByCantidadLessThan(umbral);
    }
}
