package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private ProdutoService produtoService;

    @Test
    void deveRetornarHomeComListaDeProdutos() throws Exception {
        Produto p1 = new Produto("Guitarra", 1200.0, 5, "Guitarra top");
        Produto p2 = new Produto("Bateria", 3500.0, 2, "Bateria completa");

        when(produtoService.listarTodos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("produtos"))
                .andExpect(model().attribute("produtos", List.of(p1, p2)))
                .andExpect(view().name("home"));
    }
}
