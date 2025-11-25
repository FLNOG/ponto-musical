package com.ecommerce.loja.service;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.repository.ProdutoRepository;
import com.ecommerce.loja.service.strategy.CalculadoraPreco;
import com.ecommerce.loja.service.strategy.DescontoFixo;
import com.ecommerce.loja.service.strategy.DescontoPorcentagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    private ProdutoService produtoService;
    private ProdutoRepository produtoRepository;
    private CalculadoraPreco calculadoraPreco;

    @BeforeEach
    void setup() {
        produtoRepository = mock(ProdutoRepository.class);
        calculadoraPreco = mock(CalculadoraPreco.class);

        produtoService = new ProdutoService();


        org.springframework.test.util.ReflectionTestUtils
                .setField(produtoService, "produtoRepository", produtoRepository);

        org.springframework.test.util.ReflectionTestUtils
                .setField(produtoService, "calculadoraPreco", calculadoraPreco);
    }

    @Test
    void testListarTodos() {
        List<Produto> lista = List.of(new Produto(), new Produto());
        when(produtoRepository.findAll()).thenReturn(lista);

        List<Produto> result = produtoService.listarTodos();

        assertEquals(2, result.size());
        verify(produtoRepository).findAll();
    }

    @Test
    void testBuscarPorId() {
        Produto p = new Produto();
        p.setId(1);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(p));

        Optional<Produto> result = produtoService.buscarPorId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testSalvarNovoProduto() {
        Produto novo = new Produto();
        novo.setId(0);
        novo.setNome("Guitarra");

        when(produtoRepository.save(novo)).thenReturn(novo);

        Produto salvo = produtoService.salvar(novo);

        assertEquals("Guitarra", salvo.getNome());
        verify(produtoRepository).save(novo);
    }

    @Test
    void testSalvarAtualizarProdutoExistente() {
        Produto existente = new Produto();
        existente.setId(1);
        existente.setNome("Antigo");
        existente.setPreco(100.0);

        Produto atualizado = new Produto();
        atualizado.setId(1);
        atualizado.setNome("Novo");
        atualizado.setPreco(200.0);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(existente)).thenReturn(existente);

        Produto result = produtoService.salvar(atualizado);

        assertEquals("Novo", result.getNome());
        assertEquals(200.0, result.getPreco());
        verify(produtoRepository).save(existente);
    }

    @Test
    void testDeletar() {
        produtoService.deletar(5);
        verify(produtoRepository).deleteById(5);
    }

    @Test
    void testCalcularPrecoComDescontoFixo() {
        Produto p = new Produto();
        p.setPreco(300.0);

        when(calculadoraPreco.calcularPreco(300.0)).thenReturn(250.0);

        double result = produtoService.calcularPrecoComDesconto(p, "fixo");

        verify(calculadoraPreco).setStrategy(any(DescontoFixo.class));
        assertEquals(250.0, result);
    }

    @Test
    void testCalcularPrecoComDescontoPorcentagem() {
        Produto p = new Produto();
        p.setPreco(200.0);

        when(calculadoraPreco.calcularPreco(200.0)).thenReturn(180.0);

        double result = produtoService.calcularPrecoComDesconto(p, "porcentagem");

        verify(calculadoraPreco).setStrategy(any(DescontoPorcentagem.class));
        assertEquals(180.0, result);
    }

    @Test
    void testCalcularPrecoSemStrategy() {
        Produto p = new Produto();
        p.setPreco(100.0);

        when(calculadoraPreco.calcularPreco(100.0)).thenReturn(100.0);

        double result = produtoService.calcularPrecoComDesconto(p, "nada");

        verify(calculadoraPreco).setStrategy(null);
        assertEquals(100.0, result);
    }
}
