package com.ecommerce.loja.security;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.model.TipoUsuario;
import com.ecommerce.loja.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

public class CustomLoginSuccessHandlerTest implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    public CustomLoginSuccessHandlerTest(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            response.sendRedirect("/login?error");
            return;
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/");
        }
    }
}
