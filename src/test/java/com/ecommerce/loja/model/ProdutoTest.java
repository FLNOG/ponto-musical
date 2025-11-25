package com.ecommerce.loja.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void testConstructorShouldCreateProdutoCorrectly() {
        Produto produto = new Produto("Violão", 800.0, 10, "Instrumento musical");

        assertEquals("Violão", produto.getNome());
        assertEquals(800.0, produto.getPreco());
        assertEquals(10, produto.getEstoque());
        assertEquals("Instrumento musical", produto.getDescricao());
    }


    @Test
    void testGettersAndSetters() {
        Produto produto = new Produto();

        produto.setId(1);
        produto.setNome("Guitarra");
        produto.setPreco(1500.0);
        produto.setEstoque(5);
        produto.setDescricao("Guitarra elétrica");
        produto.setImagem("imagem.jpg");

        assertEquals(1, produto.getId());
        assertEquals("Guitarra", produto.getNome());
        assertEquals(1500.0, produto.getPreco());
        assertEquals(5, produto.getEstoque());
        assertEquals("Guitarra elétrica", produto.getDescricao());
        assertEquals("imagem.jpg", produto.getImagem());
    }


    @Test
    void testNomeNaoPodeSerVazio() {
        Produto produto = new Produto("", 100.0, 1, "Teste");

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("O nome do produto é obrigatório."))
        );
    }

    @Test
    void testPrecoNaoPodeSerNulo() {
        Produto produto = new Produto();
        produto.setNome("Teclado");
        produto.setPreco(null);

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("O preço é obrigatório."))
        );
    }

    @Test
    void testPrecoDeveSerPositivo() {
        Produto produto = new Produto("Baixo", -10.0, 5, "Teste");

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("O preço deve ser positivo."))
        );
    }

    @Test
    void testEstoqueNaoPodeSerNegativo() {
        Produto produto = new Produto("Tambor", 100.0, -1, "Teste");

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("O estoque não pode ser negativo."))
        );
    }

    @Test
    void testProdutoValidoNaoDeveGerarErros() {
        Produto produto = new Produto("Flauta", 250.0, 3, "Instrumento");

        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

        assertTrue(violations.isEmpty());
    }
}
