package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;


    @Test
    void deveListarProdutos() throws Exception {
        Mockito.when(produtoService.listarTodos())
                .thenReturn(List.of(new Produto()));

        mockMvc.perform(get("/admin/produtos"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/produtos/listar"))
                .andExpect(model().attributeExists("produtos"));
    }


    @Test
    void deveExibirFormNovoProduto() throws Exception {
        mockMvc.perform(get("/admin/produtos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/produtos/form"))
                .andExpect(model().attributeExists("produto"));
    }


    @Test
    void deveSalvarProdutoValido() throws Exception {

        mockMvc.perform(post("/admin/produtos/salvar")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "Violão")
                        .param("preco", "1500")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/produtos"));

        Mockito.verify(produtoService).salvar(any(Produto.class));
    }


    @Test
    void naoDeveSalvarProdutoInvalido() throws Exception {

        mockMvc.perform(post("/admin/produtos/salvar")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "")
                        .param("preco", "0")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/produtos/form"));
    }


    @Test
    void deveEditarProdutoExistente() throws Exception {

        Produto p = new Produto();
        p.setId(1);
        p.setNome("Guitarra");

        Mockito.when(produtoService.buscarPorId(1))
                .thenReturn(Optional.of(p));

        mockMvc.perform(get("/admin/produtos/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/produtos/form"))
                .andExpect(model().attributeExists("produto"));
    }


    @Test
    void naoDeveEditarProdutoInexistente() throws Exception {

        Mockito.when(produtoService.buscarPorId(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/produtos/editar/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/produtos"));
    }


    @Test
    void deveDeletarProduto() throws Exception {

        mockMvc.perform(get("/admin/produtos/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/produtos"));

        Mockito.verify(produtoService).deletar(eq(1));
    }
}
