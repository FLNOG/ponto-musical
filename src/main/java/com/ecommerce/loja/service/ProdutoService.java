package com.ecommerce.loja.service;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.repository.ProdutoRepository;
import com.ecommerce.loja.service.strategy.CalculadoraPreco;
import com.ecommerce.loja.service.strategy.DescontoFixo;
import com.ecommerce.loja.service.strategy.DescontoPorcentagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CalculadoraPreco calculadoraPreco;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(int id) {
        return produtoRepository.findById(id);
    }

    public Produto salvar(Produto produto) {
        if (produto.getId() != 0) {
            Optional<Produto> existente = produtoRepository.findById(produto.getId());
            if (existente.isPresent()) {
                Produto p = existente.get();
                p.setNome(produto.getNome());
                p.setPreco(produto.getPreco());
                p.setEstoque(produto.getEstoque());
                p.setDescricao(produto.getDescricao());
                p.setImagem(produto.getImagem());
                return produtoRepository.save(p);
            }
        }
        return produtoRepository.save(produto);
    }

    public void deletar(int id) {
        produtoRepository.deleteById(id);
    }

    // Exemplo do uso do Strategy Pattern
    public double calcularPrecoComDesconto(Produto produto, String tipoDesconto) {
        switch (tipoDesconto.toLowerCase()) {
            case "fixo" -> calculadoraPreco.setStrategy(new DescontoFixo(50.0));
            case "porcentagem" -> calculadoraPreco.setStrategy(new DescontoPorcentagem(10.0));
            default -> calculadoraPreco.setStrategy(null);
        }
        return calculadoraPreco.calcularPreco(produto.getPreco());
    }
}