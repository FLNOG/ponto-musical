package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.TipoUsuario;
import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios/listar"; // templates/admin/usuarios/listar.html
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("tipos", TipoUsuario.values());
        return "admin/usuarios/form"; // templates/admin/usuarios/form.html
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Usuario usuario, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/usuarios/form";
        }

        if(usuarioService.cpfExiste(usuario.getCpf()) && usuario.getId() == null) {
            result.rejectValue("cpf", "error.usuario", "CPF já cadastrado!");
            return "admin/usuarios/form";
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        usuarioService.salvar(usuario);
        ra.addFlashAttribute("sucesso", "Usuário salvo com sucesso!");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/email/{email}")
    @ResponseBody
    public Optional<Usuario> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email);
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("tipos", TipoUsuario.values());
            return "admin/usuarios/form";
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id, RedirectAttributes ra) {
        usuarioService.deletar(id);
        ra.addFlashAttribute("sucesso", "Usuário removido com sucesso!");
        return "redirect:/admin/usuarios";
    }
}