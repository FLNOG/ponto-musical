package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private UsuarioService usuarioService;


    private PasswordEncoder passwordEncoder;

    @Test
    void testListarUsuarios() throws Exception {
        Mockito.when(usuarioService.listarTodos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/usuarios/listar"))
                .andExpect(model().attributeExists("usuarios"));
    }

    @Test
    void testNovoUsuarioForm() throws Exception {
        mockMvc.perform(get("/admin/usuarios/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/usuarios/form"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("tipos"));
    }

    @Test
    void testSalvarUsuarioValido() throws Exception {
        Mockito.when(usuarioService.cpfExiste("12345678900")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("123")).thenReturn("encoded123");

        mockMvc.perform(post("/admin/usuarios/salvar")
                        .param("nome", "João")
                        .param("email", "joao@gmail.com")
                        .param("senha", "123")
                        .param("cpf", "12345678900"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/usuarios"));
    }

    @Test
    void testSalvarUsuarioComErrosValidacao() throws Exception {
        mockMvc.perform(post("/admin/usuarios/salvar")
                        .param("nome", "")
                        .param("email", "email_invalido")
                        .param("senha", "")
                        .param("cpf", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/usuarios/form"));
    }

    @Test
    void testSalvarUsuarioCpfDuplicado() throws Exception {
        Mockito.when(usuarioService.cpfExiste("11111111111")).thenReturn(true);

        mockMvc.perform(post("/admin/usuarios/salvar")
                        .param("nome", "Maria")
                        .param("email", "maria@gmail.com")
                        .param("senha", "123")
                        .param("cpf", "11111111111"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/usuarios/form"));
    }

    @Test
    void testBuscarPorEmail() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@gmail.com");

        Mockito.when(usuarioService.buscarPorEmail("teste@gmail.com"))
                .thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/admin/usuarios/email/teste@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"teste@gmail.com\"}"));
    }

    @Test
    void testEditarUsuarioExistente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("João");

        Mockito.when(usuarioService.buscarPorId(1))
                .thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/admin/usuarios/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/usuarios/form"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("tipos"));
    }

    @Test
    void testEditarUsuarioNaoEncontrado() throws Exception {
        Mockito.when(usuarioService.buscarPorId(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/usuarios/editar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/usuarios"));
    }

    @Test
    void testDeletarUsuario() throws Exception {
        mockMvc.perform(get("/admin/usuarios/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/usuarios"));
    }
}
