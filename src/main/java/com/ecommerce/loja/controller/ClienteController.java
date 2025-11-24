package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.TipoUsuario;
import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ClienteController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clientes/cadastrar")
    public String mostrarFormCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cliente/cadastro";
    }
    @GetMapping("/minha_conta")
    public String minhaConta(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cliente/minha_conta";
    }

    @PostMapping("/clientes/cadastrar")
    public String cadastrarCliente(Usuario usuario, Model model) {

        if (usuarioService.cpfExiste(usuario.getCpf())) {
            model.addAttribute("erro", "CPF já cadastrado.");
            return "cliente/cadastro";
        }

        if (usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("erro", "E-mail já cadastrado.");
            return "cliente/cadastro";
        }

        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioService.salvar(usuario);

        return "redirect:/login?cadastroSucesso";
    }
}