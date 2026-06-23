package com.prueba.envio;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prueba.envio.Client.PedidoFeignClient;
import com.prueba.envio.Model.DTO.PedidoDTO;
import com.prueba.envio.Model.Envio;
import com.prueba.envio.Repository.EnvioRepository;
import com.prueba.envio.Service.EnvioService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EnvioServiceTest {

    @InjectMocks
    private EnvioService envioService;

    @Mock
    private EnvioRepository repository;

    @Mock
    private PedidoFeignClient pedidoClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Given
        Envio envio = new Envio();
        when(repository.save(envio)).thenReturn(envio);

        // When
        Envio saved = envioService.save(envio);

        // Then
        assertNotNull(saved);
        verify(repository, times(1)).save(envio);
    }

    @Test
    public void testFindAll() {
        // Given
        Envio envio = new Envio();
        when(repository.findAll()).thenReturn(List.of(envio));

        // When
        List<Envio> envios = envioService.findAll();

        // Then
        assertNotNull(envios);
        assertEquals(1, envios.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testBuscarEnvioCompleto_Encontrado() {
        // Given
        Integer id = 1;
        Envio envio = new Envio();
        envio.setId(id);
        envio.setPedidoId(20);
        envio.setDireccion("Av. Siempre Viva 123");
        envio.setEstado("EN_CAMINO");
        envio.setTrackingCode("TRK-001");
        envio.setFechaCreacion(new Date());

        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(20);

        when(repository.findById(id)).thenReturn(Optional.of(envio));
        when(pedidoClient.obtenerPedidoPorId(20)).thenReturn(pedido);

        // When
        Map<String, Object> respuesta = envioService.buscarEnvioCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertEquals(id, respuesta.get("id"));
        assertEquals(20, respuesta.get("pedidoId"));
        assertEquals("Av. Siempre Viva 123", respuesta.get("direccion"));
        assertEquals("EN_CAMINO", respuesta.get("estado"));
        assertEquals(pedido, respuesta.get("pedido"));

        verify(repository, times(1)).findById(id);
        verify(pedidoClient, times(1)).obtenerPedidoPorId(20);
    }

    @Test
    public void testBuscarEnvioCompleto_PedidoNoDisponible() {
        // Given
        Integer id = 1;
        Envio envio = new Envio();
        envio.setId(id);
        envio.setPedidoId(20);

        when(repository.findById(id)).thenReturn(Optional.of(envio));
        when(pedidoClient.obtenerPedidoPorId(20)).thenThrow(new RuntimeException("Servicio no disponible"));

        // When
        Map<String, Object> respuesta = envioService.buscarEnvioCompleto(id);

        // Then
        assertEquals("No disponible", respuesta.get("pedido"));
        verify(pedidoClient, times(1)).obtenerPedidoPorId(20);
    }

    @Test
    public void testBuscarEnvioCompleto_NoExiste() {
        // Given
        Integer id = 99;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Map<String, Object> respuesta = envioService.buscarEnvioCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertTrue(respuesta.isEmpty());
        verify(repository, times(1)).findById(id);
        verifyNoInteractions(pedidoClient);
    }

    @Test
    public void testDelete() {
        // Given
        Integer id = 1;
        doNothing().when(repository).deleteById(id);

        // When
        envioService.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testFiltrarPorEstado() {
        // Given
        String estado = "ENTREGADO";
        Envio envio = new Envio();
        envio.setEstado(estado);
        when(repository.findByEstado(estado)).thenReturn(List.of(envio));

        // When
        List<Envio> resultado = envioService.filtrarPorEstado(estado);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByEstado(estado);
    }

    @Test
    public void testFiltrarPorPedido() {
        // Given
        Integer pedidoId = 20;
        Envio envio = new Envio();
        envio.setPedidoId(pedidoId);
        when(repository.findByPedidoId(pedidoId)).thenReturn(List.of(envio));

        // When
        List<Envio> resultado = envioService.filtrarPorPedido(pedidoId);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByPedidoId(pedidoId);
    }
}
