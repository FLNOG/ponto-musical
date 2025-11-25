package com.ecommerce.loja.controller;


import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private UsuarioService usuarioService;


    private PasswordEncoder passwordEncoder;

    @Test
    void deveAbrirPaginaCadastro() throws Exception {
        mockMvc.perform(get("/clientes/cadastrar"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuario"))
                .andExpect(view().name("cliente/cadastro"));
    }

    @Test
    void deveAbrirMinhaConta() throws Exception {
        mockMvc.perform(get("/minha_conta"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuario"))
                .andExpect(view().name("cliente/minha_conta"));
    }

    @Test
    void deveAbrirMeusPedidos() throws Exception {
        mockMvc.perform(get("/meus_pedidos"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("pedidos"))
                .andExpect(view().name("cliente/meus_pedidos"));
    }

    @Test
    void deveCadastrarClienteComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setCpf("12345678900");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("123");

        when(usuarioService.cpfExiste("12345678900")).thenReturn(false);
        when(usuarioService.buscarPorEmail("teste@teste.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("senhaCodificada");
        when(usuarioService.salvar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/clientes/cadastrar")
                        .param("cpf", "12345678900")
                        .param("email", "teste@teste.com")
                        .param("senha", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?cadastroSucesso"));
    }

    @Test
    void deveRetornarErroCPFJaExiste() throws Exception {
        when(usuarioService.cpfExiste("11122233344")).thenReturn(true);

        mockMvc.perform(post("/clientes/cadastrar")
                        .param("cpf", "11122233344")
                        .param("email", "teste@teste.com")
                        .param("senha", "123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("erro"))
                .andExpect(view().name("cliente/cadastro"));
    }

    @Test
    void deveRetornarErroEmailJaExiste() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("existente@teste.com");

        when(usuarioService.cpfExiste("11122233355")).thenReturn(false);
        when(usuarioService.buscarPorEmail("existente@teste.com"))
                .thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/clientes/cadastrar")
                        .param("cpf", "11122233355")
                        .param("email", "existente@teste.com")
                        .param("senha", "123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("erro"))
                .andExpect(view().name("cliente/cadastro"));
    }
}
