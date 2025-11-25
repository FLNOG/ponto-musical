package com.ecommerce.loja.repository;

import com.ecommerce.loja.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveSalvarProduto() {
        Produto p = new Produto();
        p.setNome("Violão Yamaha");
        p.setPreco(1200.0);

        Produto salvo = produtoRepository.save(p);

        assertNotNull(salvo.getId());
        assertEquals("Violão Yamaha", salvo.getNome());
    }

    @Test
    void deveListarProdutos() {
        Produto p = new Produto();
        p.setNome("Teclado Roland");
        p.setPreco(3500.0);

        produtoRepository.save(p);

        assertFalse(produtoRepository.findAll().isEmpty());
    }
}
