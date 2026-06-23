package com.prueba.busqueda;

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

import com.prueba.busqueda.Client.RopaFeignClient;
import com.prueba.busqueda.Model.Busqueda;
import com.prueba.busqueda.Model.DTO.RopaDTO;
import com.prueba.busqueda.Repository.BusquedaRepository;
import com.prueba.busqueda.Service.BusquedaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BusquedaServiceTest {

    @InjectMocks
    private BusquedaService busquedaService;

    @Mock
    private BusquedaRepository repository;

    @Mock
    private RopaFeignClient ropaClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Given
        Busqueda busqueda = new Busqueda();
        when(repository.save(busqueda)).thenReturn(busqueda);

        // When
        Busqueda saved = busquedaService.save(busqueda);

        // Then
        assertNotNull(saved);
        verify(repository, times(1)).save(busqueda);
    }

    @Test
    public void testFindAll() {
        // Given
        Busqueda busqueda = new Busqueda();
        when(repository.findAll()).thenReturn(List.of(busqueda));

        // When
        List<Busqueda> busquedas = busquedaService.findAll();

        // Then
        assertNotNull(busquedas);
        assertEquals(1, busquedas.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testBuscarBusquedaCompleto_Encontrado() {
        // Given
        Integer id = 1;
        Busqueda busqueda = new Busqueda();
        busqueda.setId(id);
        busqueda.setCategoria("Poleras");
        busqueda.setResultadosIds(List.of(7, 8));

        RopaDTO ropa1 = new RopaDTO();
        RopaDTO ropa2 = new RopaDTO();

        when(repository.findById(id)).thenReturn(Optional.of(busqueda));
        when(ropaClient.obtenerRopaPorId(7)).thenReturn(ropa1);
        when(ropaClient.obtenerRopaPorId(8)).thenReturn(ropa2);

        // When
        Map<String, Object> respuesta = busquedaService.buscarBusquedaCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertEquals(id, respuesta.get("id"));
        assertEquals("Poleras", respuesta.get("categoria"));

        @SuppressWarnings("unchecked")
        List<RopaDTO> resultados = (List<RopaDTO>) respuesta.get("resultados");
        assertEquals(2, resultados.size());

        verify(repository, times(1)).findById(id);
        verify(ropaClient, times(1)).obtenerRopaPorId(7);
        verify(ropaClient, times(1)).obtenerRopaPorId(8);
    }

    @Test
    public void testBuscarBusquedaCompleto_NoExiste() {
        // Given
        Integer id = 99;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Map<String, Object> respuesta = busquedaService.buscarBusquedaCompleto(id);

        // Then
        assertNotNull(respuesta);
        assertTrue(respuesta.isEmpty());
        verify(repository, times(1)).findById(id);
        verify(ropaClient, never()).obtenerRopaPorId(any());
    }

    @Test
    public void testDelete() {
        // Given
        Integer id = 1;
        doNothing().when(repository).deleteById(id);

        // When
        busquedaService.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testFiltrarPorCategoria() {
        // Given
        String categoria = "Pantalones";
        Busqueda busqueda = new Busqueda();
        busqueda.setCategoria(categoria);
        when(repository.findByCategoria(categoria)).thenReturn(List.of(busqueda));

        // When
        List<Busqueda> resultado = busquedaService.filtrarPorCategoria(categoria);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByCategoria(categoria);
    }
}
