package com.ecommerce.loja.service;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void testListarTodos() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Usuario u = new Usuario();
        u.setId(1);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));

        Optional<Usuario> resultado = usuarioService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        verify(usuarioRepository).findById(1);
    }

    @Test
    void testSalvar() {
        Usuario u = new Usuario();
        u.setNome("Theo");

        when(usuarioRepository.save(u)).thenReturn(u);

        Usuario salvo = usuarioService.salvar(u);

        assertEquals("Theo", salvo.getNome());
        verify(usuarioRepository).save(u);
    }

    @Test
    void testDeletar() {
        usuarioService.deletar(10);
        verify(usuarioRepository).deleteById(10);
    }

    @Test
    void testBuscarPorEmail() {
        Usuario u = new Usuario();
        u.setEmail("teste@teste.com");

        when(usuarioRepository.findByEmail("teste@teste.com"))
                .thenReturn(Optional.of(u));

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("teste@teste.com");

        assertTrue(resultado.isPresent());
        assertEquals("teste@teste.com", resultado.get().getEmail());
        verify(usuarioRepository).findByEmail("teste@teste.com");
    }

    @Test
    void testCpfExiste_True() {
        Usuario u = new Usuario();
        u.setCpf("12345678900");

        when(usuarioRepository.findByCpf("12345678900"))
                .thenReturn(Optional.of(u));

        assertTrue(usuarioService.cpfExiste("12345678900"));
    }

    @Test
    void testCpfExiste_False() {
        when(usuarioRepository.findByCpf("11122233344"))
                .thenReturn(Optional.empty());

        assertFalse(usuarioService.cpfExiste("11122233344"));
    }
}
