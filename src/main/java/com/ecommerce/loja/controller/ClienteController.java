package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings("all")
@Controller
public class ClienteController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastro_cliente")
    public String exibirCadastro_cliente(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cliente/cliente_cadastro";
    }

    @PostMapping("/cadastro_cliente")
    public String cadastrarCliente(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (!usuario.getSenha().equals(usuario.getConfirmarSenha())) {
                model.addAttribute("erro", "Senhas não coincidem");
                return "cliente/cliente_cadastro";
            }

            usuario.setPerfil(Usuario.Perfil.CLIENTE);

            usuarioService.cadastro(usuario);

            model.addAttribute("sucesso", "Usuário cadastrado com sucesso");
            model.addAttribute("usuario", new Usuario());
            return "redirect:/";

        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "cliente/cliente_cadastro";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/login_cliente")
    public String exibirLogin_cliente(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cliente/cliente_login";
    }

    @PostMapping("/login_cliente")
    public String login_cliente(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (usuario.getEmail() == null || usuario.getEmail().isBlank() ||
                    usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                model.addAttribute("erro", "Campo em branco");
                return "cliente/cliente_login";
            }

            Usuario u = usuarioService.findByEmail(usuario.getEmail());
            if (u == null) {
                model.addAttribute("erro", "Usuário não cadastrado");
                return "cliente/cliente_login";
            }

            if (!u.getSenha().equals(usuarioService.codificaSenha(usuario.getSenha()))) {
                model.addAttribute("erro", "Email ou senha incorretos");
                return "cliente/cliente_login";
            }

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "cliente/cliente_login";
        }

        return "redirect:/";
    }
}