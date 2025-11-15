package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String verCarrinho(HttpSession session, Model model) {
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        model.addAttribute("carrinho", carrinho);
        return "cliente/carrinho/listar"; // Aqui precisa existir o template
    }

    @PostMapping("/adicionar")
    public String adicionarProduto(@RequestParam int produtoId, HttpSession session) {
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        Optional<Produto> produto = produtoService.buscarPorId(produtoId);
        produto.ifPresent(carrinho::add);
        session.setAttribute("carrinho", carrinho);
        return "redirect:/carrinho";
    }

    @PostMapping("/remover")
    public String removerProduto(@RequestParam int produtoId, HttpSession session) {
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
        if (carrinho != null) {
            carrinho.removeIf(p -> p.getId() == produtoId);
            session.setAttribute("carrinho", carrinho);
        }

        return "cliente/carrinho/listar";
    }
}
