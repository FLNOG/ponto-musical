package com.ecommerce.loja.security;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UsuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("123456");

        when(usuarioService.buscarPorEmail("teste@teste.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername("teste@teste.com");

        assertNotNull(userDetails);
        assertEquals("teste@teste.com", userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());

        verify(usuarioService, times(1)).buscarPorEmail("teste@teste.com");
    }

    @Test
    void testLoadUserByUsername_UsuarioNaoEncontrado() {
        when(usuarioService.buscarPorEmail("naoexiste@teste.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("naoexiste@teste.com")
        );

        verify(usuarioService, times(1)).buscarPorEmail("naoexiste@teste.com");
    }
}
