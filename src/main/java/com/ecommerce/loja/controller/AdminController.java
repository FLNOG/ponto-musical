package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@SuppressWarnings("all")
@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/adm_dashboard")
    public String dashboard() {
        return "adm/adm_dashboard";
    }

    @GetMapping("/adm_relatorio_produtos")
    public String relatorioProdutos() {
        return "adm/adm_relatorio_produtos";
    }

    @GetMapping("/adm_relatorio_vendas")
    public String relatorioVendas() {
        return "adm/adm_relatorio_vendas";
    }

    @GetMapping("/adm/clientes")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("clientes", usuarios);

        return "adm/adm_clientes";
    }

    @GetMapping("/adm/clientes/editar/{id}")
    public String editarUsuarioForm(@PathVariable int id, Model model) throws Exception {
        Usuario usuario = usuarioService.findById(id);
        model.addAttribute("usuario", usuario);
        return "adm/adm_editar_usuario";
    }

    @PostMapping("/adm/clientes/editar/{id}")
    public String editarUsuario(@PathVariable int id, @ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            usuarioService.editar(id, usuario);
            model.addAttribute("sucesso", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
        }
        return "adm/adm_editar_usuario";
    }

    @GetMapping("/login_admin")
    public String exibirLogin_admin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "adm/adm_login";
    }

    @PostMapping("/login_admin")
    public String login_admin(@ModelAttribute Usuario usuario, Model model) {
        try {
            if (usuario.getEmail() == null || usuario.getEmail().isBlank() ||
                    usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                model.addAttribute("erro", "Campo em branco");
                return "adm/adm_login";
            }

            Usuario u = usuarioService.findByEmail(usuario.getEmail());
            if (u == null) {
                model.addAttribute("erro", "Usuário não cadastrado");
                return "adm/adm_login";
            }

            if (!u.getSenha().equals(usuarioService.codificaSenha(usuario.getSenha()))) {
                model.addAttribute("erro", "Email ou senha incorretos");
                return "adm/adm_login";
            }

            if (!Usuario.Perfil.ADMINISTRADOR.equals(u.getPerfil())) {
                model.addAttribute("erro", "Acesso permitido apenas para administradores");
                return "adm/adm_login";
            }

            return "redirect:/adm_dashboard";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "adm/adm_login";
        }
    }
}