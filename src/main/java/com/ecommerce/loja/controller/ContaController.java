package com.ecommerce.loja.controller;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.security.CustomUserDetails;
import com.ecommerce.loja.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContaController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String perfil(Model model, @AuthenticationPrincipal Usuario usuario) {

        model.addAttribute("usuario", usuario);
        return "cliente/minha_conta";
    }

    @GetMapping("/usuario/logado")
    @ResponseBody
    public Usuario usuarioLogado(Authentication auth) {
        CustomUserDetails cd = (CustomUserDetails) auth.getPrincipal();
        return cd.getUsuario();
    }

    @PostMapping("/usuario/atualizar")
    public String atualizarUsuario(Authentication auth,
                                   @RequestParam("nome") String nome,
                                   @RequestParam("telefone") String telefone,
                                   @RequestParam("dataNascimento") String dataNascimento) {

        CustomUserDetails cd = (CustomUserDetails) auth.getPrincipal();
        String emailLogado = cd.getUsername();

        Usuario usuarioAtualizado = usuarioService.buscarPorEmail(emailLogado)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        usuarioAtualizado.setNome(nome);
        usuarioAtualizado.setTelefone(telefone);
        usuarioAtualizado.setDataNascimento(dataNascimento);

        usuarioService.salvar(usuarioAtualizado);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails novoUserDetails = new CustomUserDetails(usuarioAtualizado);
        Authentication novaAutenticacao = new UsernamePasswordAuthenticationToken(
                novoUserDetails,
                authentication.getCredentials(),
                novoUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(novaAutenticacao);
        return "redirect:/perfil?sucesso";
    }
}