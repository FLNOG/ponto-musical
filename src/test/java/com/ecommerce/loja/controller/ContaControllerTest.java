package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.security.CustomUserDetails;
import com.ecommerce.loja.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContaController.class)
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private UsuarioService usuarioService;

    private Usuario usuario;
    private CustomUserDetails userDetails;
    private Authentication authMock;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setEmail("teste@teste.com");
        usuario.setNome("Usuário Teste");

        userDetails = new CustomUserDetails(usuario);

        authMock = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    @Test
    void deveRetornarPaginaPerfil() throws Exception {
        mockMvc.perform(get("/perfil").principal(authMock))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuario"))
                .andExpect(view().name("cliente/minha_conta"));
    }

    @Test
    void deveRetornarUsuarioLogado() throws Exception {
        mockMvc.perform(get("/usuario/logado").principal(authMock))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teste@teste.com"));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        when(usuarioService.buscarPorEmail("teste@teste.com"))
                .thenReturn(Optional.of(usuario));
        when(usuarioService.salvar(any(Usuario.class)))
                .thenReturn(usuario);

        // Mock para o SecurityContextHolder
        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {

            SecurityContext securityContext = mock(SecurityContext.class);

            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authMock);

            mockMvc.perform(post("/usuario/atualizar")
                            .principal(authMock)
                            .param("nome", "Novo Nome")
                            .param("telefone", "9999-9999")
                            .param("dataNascimento", "2000-01-01"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/perfil?sucesso"));

            verify(usuarioService).salvar(any(Usuario.class));
        }
    }
}
