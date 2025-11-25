package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarrinhoController.class)
class CarrinhoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private ProdutoService produtoService;

    private MockHttpSession session;

    @BeforeEach
    void setup() {
        session = new MockHttpSession();
    }

    @Test
    void deveExibirCarrinhoVazio() throws Exception {
        mockMvc.perform(get("/carrinho").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carrinho"))
                .andExpect(view().name("cliente/carrinho/listar"));
    }

    @Test
    void deveAdicionarProdutoAoCarrinho() throws Exception {
        Produto produto = new Produto("Guitarra", 1200.0, 5, "Guitarra top");
        produto.setId(1);

        when(produtoService.buscarPorId(1)).thenReturn(Optional.of(produto));

        mockMvc.perform(post("/carrinho/adicionar")
                        .param("produtoId", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carrinho"));

        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

        assert carrinho != null;
        assert carrinho.size() == 1;
        assert carrinho.get(0).getId() == 1;
    }

    @Test
    void deveRemoverProdutoDoCarrinho() throws Exception {
        Produto p = new Produto("Teclado", 900.0, 10, "Teclado mecânico");
        p.setId(2);

        List<Produto> carrinho = new ArrayList<>();
        carrinho.add(p);

        session.setAttribute("carrinho", carrinho);

        mockMvc.perform(post("/carrinho/remover")
                        .param("produtoId", "2")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carrinho"));

        List<Produto> carrinhoAtualizado = (List<Produto>) session.getAttribute("carrinho");

        assert carrinhoAtualizado != null;
        assert carrinhoAtualizado.isEmpty();
    }
}

