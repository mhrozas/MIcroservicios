package com.prueba.inventario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.prueba.inventario.Client.RopaFeignClient;
import com.prueba.inventario.Model.Inventario;
import com.prueba.inventario.Model.DTO.RopaDTO;
import com.prueba.inventario.Repository.InventarioRepository;
import com.prueba.inventario.Service.InventarioService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class IntentarioServiceTest {
@Mock
    private InventarioRepository repository;

    @Mock
    private RopaFeignClient ropaClient;

    @InjectMocks
    private InventarioService inventarioService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Inventario inventarioInput = new Inventario();
        when(repository.save(any(Inventario.class))).thenReturn(inventarioInput);

        Inventario resultado = inventarioService.save(inventarioInput);

        assertNotNull(resultado);
        verify(repository, times(1)).save(inventarioInput);
    }

    @Test
    void testFindAll() {
        List<Inventario> listaSimulada = Arrays.asList(new Inventario(), new Inventario());
        when(repository.findAll()).thenReturn(listaSimulada);

        List<Inventario> resultado = inventarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testBuscarInventarioCompletoExitoso() {
        Integer inventarioId = 1;
        Integer ropaId = 45;

        Inventario inventarioMock = new Inventario();
        inventarioMock.setId(inventarioId);
        inventarioMock.setNombre("Poleras Algodón");
        inventarioMock.setDetalle("Talla M");
        inventarioMock.setCantidad(50);
        inventarioMock.setFechaCreacion(new Date());
        inventarioMock.setRopaId(ropaId);

        RopaDTO ropaDTOMock = new RopaDTO();

        when(repository.findById(inventarioId)).thenReturn(Optional.of(inventarioMock));
        when(ropaClient.obtenerRopaPorId(ropaId)).thenReturn(ropaDTOMock);

        Map<String, Object> respuesta = inventarioService.buscarInventarioCompleto(inventarioId);

        assertNotNull(respuesta);
        assertEquals(inventarioId, respuesta.get("id"));
        assertEquals("Poleras Algodón", respuesta.get("nombre"));
        assertEquals(50, respuesta.get("cantidad"));
        assertEquals(ropaDTOMock, respuesta.get("ropa"));
        verify(ropaClient, times(1)).obtenerRopaPorId(ropaId);
    }

    @Test
    void testDelete() {
        Integer idTest = 10;
        doNothing().when(repository).deleteById(idTest);

        inventarioService.delete(idTest);

        verify(repository, times(1)).deleteById(idTest);
    }

    @Test
    void testActualizarCantidadExitoso() {
        Integer idTest = 2;
        Integer nuevaCantidad = 150;

        Inventario inventarioMock = new Inventario();
        inventarioMock.setId(idTest);
        inventarioMock.setCantidad(20);

        when(repository.findById(idTest)).thenReturn(Optional.of(inventarioMock));
        when(repository.save(any(Inventario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.actualizarCantidad(idTest, nuevaCantidad);

        assertNotNull(resultado);
        assertEquals(nuevaCantidad, resultado.getCantidad());
        verify(repository, times(1)).save(inventarioMock);
    }

    @Test
    void testActualizarCantidadNoEncontradoLanzaExcepcion() {
        Integer idTest = 99;
        when(repository.findById(idTest)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            inventarioService.actualizarCantidad(idTest, 100);
        });

        assertEquals("Inventario no encontrado id: " + idTest, excepcion.getMessage());
        verify(repository, never()).save(any(Inventario.class));
    }

    @Test
    void testVerificarStockConStockDisponible() {
        Integer ropaIdTest = 77;
        Inventario inventarioMock = new Inventario();
        inventarioMock.setCantidad(10);

        when(repository.findByRopaId(ropaIdTest)).thenReturn(Collections.singletonList(inventarioMock));

        Map<String, Object> respuesta = inventarioService.verificarStock(ropaIdTest);

        assertNotNull(respuesta);
        assertEquals(ropaIdTest, respuesta.get("ropaId"));
        assertEquals(10, respuesta.get("cantidad"));
        assertEquals(true, respuesta.get("disponible"));
    }

    @Test
    void testVerificarStockNoEncontradoLanzaExcepcion() {
        Integer ropaIdTest = 88;
        when(repository.findByRopaId(ropaIdTest)).thenReturn(new ArrayList<>());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            inventarioService.verificarStock(ropaIdTest);
        });

        assertEquals("Producto no encontrado en inventario", excepcion.getMessage());
    }

    @Test
    void testListarStockBajo() {

        Integer limiteBajo = 5;
        List<Inventario> listaSimulada = Arrays.asList(new Inventario(), new Inventario());
        when(repository.findByCantidadLessThan(limiteBajo)).thenReturn(listaSimulada);

        List<Inventario> resultado = inventarioService.listarStockBajo(limiteBajo);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findByCantidadLessThan(limiteBajo);
    }
}
