package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "admin/produtos/listar"; // templates/admin/produtos/listar.html
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        return "admin/produtos/form"; // templates/admin/produtos/form.html
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Produto produto, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/produtos/form";
        }
        produtoService.salvar(produto);
        ra.addFlashAttribute("sucesso", "Produto salvo com sucesso!");
        return "redirect:/admin/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Optional<Produto> produto = produtoService.buscarPorId(id);
        if (produto.isPresent()) {
            model.addAttribute("produto", produto.get());
            return "admin/produtos/form";
        }
        return "redirect:/admin/produtos";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id, RedirectAttributes ra) {
        produtoService.deletar(id);
        ra.addFlashAttribute("sucesso", "Produto removido com sucesso!");
        return "redirect:/admin/produtos";
    }
}