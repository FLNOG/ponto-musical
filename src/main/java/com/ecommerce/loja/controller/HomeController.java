package com.ecommerce.loja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("all")
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
