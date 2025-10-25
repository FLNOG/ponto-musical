package com.ecommerce.loja.security;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();
        String nome = userDetails.getNome();

        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

        if (usuario != null) {
            if (usuario.getTipo().name().equals("ADMIN")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/login?error");
        }
    }
}