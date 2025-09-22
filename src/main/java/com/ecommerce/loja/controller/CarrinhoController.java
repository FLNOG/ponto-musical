package com.ecommerce.loja.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CarrinhoController {

    @GetMapping("/carrinho")
    public String Carrinho() {
        return "carrinho/carrinho";
    }
}
