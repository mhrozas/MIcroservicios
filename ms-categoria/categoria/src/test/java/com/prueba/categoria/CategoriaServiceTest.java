package com.prueba.categoria;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prueba.categoria.Model.Categoria;
import com.prueba.categoria.Repository.CategoriaRepository;
import com.prueba.categoria.Service.CategoriaService;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Given
        Categoria categoria = new Categoria();
        categoria.setNombre("Poleras");
        when(repository.save(categoria)).thenReturn(categoria);

        // When
        Categoria saved = categoriaService.save(categoria);

        // Then
        assertNotNull(saved);
        assertEquals("Poleras", saved.getNombre());
        verify(repository, times(1)).save(categoria);
    }

    @Test
    public void testFindAll() {
        // Given
        Categoria categoria = new Categoria();
        when(repository.findAll()).thenReturn(List.of(categoria));

        // When
        List<Categoria> categorias = categoriaService.findAll();

        // Then
        assertNotNull(categorias);
        assertEquals(1, categorias.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testFindById_Encontrado() {
        // Given
        Integer id = 1;
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre("Pantalones");
        when(repository.findById(id)).thenReturn(Optional.of(categoria));

        // When
        Optional<Categoria> resultado = categoriaService.findById(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Pantalones", resultado.get().getNombre());
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NoEncontrado() {
        // Given
        Integer id = 99;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Categoria> resultado = categoriaService.findById(id);

        // Then
        assertFalse(resultado.isPresent());
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testUpdate() {
        // Given
        Integer id = 1;
        Categoria categoria = new Categoria();
        categoria.setNombre("Chaquetas");
        when(repository.save(categoria)).thenReturn(categoria);

        // When
        Categoria actualizado = categoriaService.update(id, categoria);

        // Then
        assertNotNull(actualizado);
        assertEquals(id, categoria.getId());
        verify(repository, times(1)).save(categoria);
    }

    @Test
    public void testDelete() {
        // Given
        Integer id = 1;
        doNothing().when(repository).deleteById(id);

        // When
        categoriaService.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }
}
