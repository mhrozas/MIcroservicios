package com.prueba.busqueda;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Test
    public void testSave() {
        Busqueda busqueda = new Busqueda();
        // Define el comportamiento del mock: al guardar, retorna el mismo objeto.
        when(repository.save(busqueda)).thenReturn(busqueda);

        // Llama al método save() del servicio.
        Busqueda saved = busquedaService.save(busqueda);

        // Verifica que no sea nulo. Si asignaste un ID o categoría arriba, puedes hacer un assertEquals aquí.
        assertNotNull(saved);
        verify(repository, times(1)).save(busqueda);
    }

    @Test
    public void testFindAll() {
        Busqueda busqueda = new Busqueda();
        
        // Define el comportamiento del mock: devuelve una lista con una Búsqueda.
        when(repository.findAll()).thenReturn(List.of(busqueda));

        // Llama al método findAll() del servicio.
        List<Busqueda> busquedas = busquedaService.findAll();

        // Verifica que la lista no sea nula y tenga el tamaño correcto.
        assertNotNull(busquedas);
        assertEquals(1, busquedas.size());
    }

    @Test
    public void testBuscarBusquedaCompleto() {
        Integer busquedaId = 1;
        Busqueda busqueda = new Busqueda();
        // Asumiendo que puedes setear estos valores según tu entidad
        // busqueda.setId(busquedaId);
        // busqueda.setCategoria("Zapatos");
        // busqueda.setResultadosIds(List.of(100, 101)); 

        RopaDTO ropa1 = new RopaDTO();
        RopaDTO ropa2 = new RopaDTO();

        // 1. Simulamos que el repositorio encuentra la búsqueda
        when(repository.findById(busquedaId)).thenReturn(Optional.of(busqueda));
        
        // 2. Simulamos las llamadas al FeignClient por cada ID en getResultadosIds()
        // NOTA: Si descomentas setResultadosIds arriba con 100 y 101, descomenta lo siguiente:
        // when(ropaClient.obtenerRopaPorId(100)).thenReturn(ropa1);
        // when(ropaClient.obtenerRopaPorId(101)).thenReturn(ropa2);

        // Llama al método complejo del servicio.
        Map<String, Object> respuesta = busquedaService.buscarBusquedaCompleto(busquedaId);

        // Verifica que el Map devuelto tenga los datos correctos
        assertNotNull(respuesta);
        // Descomenta las validaciones según cómo construyas tu objeto 'busqueda' arriba
        // assertEquals(busquedaId, respuesta.get("id"));
        // assertEquals("Zapatos", respuesta.get("categoria"));
        // List<RopaDTO> resultados = (List<RopaDTO>) respuesta.get("resultados");
        // assertEquals(2, resultados.size());
    }

    @Test
    public void testDelete() {
        Integer id = 1;

        // Define el comportamiento del mock: cuando se llame a deleteById(), no hace nada.
        doNothing().when(repository).deleteById(id);

        // Llama al método delete() del servicio.
        busquedaService.delete(id);

        // Verifica que el método del repositorio se haya llamado exactamente una vez.
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testFiltrarPorCategoria() {
        String categoria = "Pantalones";
        Busqueda busqueda = new Busqueda();
        // busqueda.setCategoria(categoria);

        // Define el comportamiento del mock.
        when(repository.findByCategoria(categoria)).thenReturn(List.of(busqueda));

        // Llama al método filtrarPorCategoria() del servicio.
        List<Busqueda> encontradas = busquedaService.filtrarPorCategoria(categoria);

        // Verifica la respuesta.
        assertNotNull(encontradas);
        assertEquals(1, encontradas.size());
        verify(repository, times(1)).findByCategoria(categoria);
    }
}
