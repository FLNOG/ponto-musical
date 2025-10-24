package com.ecommerce.loja.config;

import com.ecommerce.loja.model.TipoUsuario;
import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
//public class DataLoader {
//
//    @Bean
//    public CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (usuarioRepository.count() == 0) {
//                Usuario admin = new Usuario();
//                admin.setEmail("admin@loja.com");
//                admin.setSenha(passwordEncoder.encode("123456"));
//                admin.setTipo(TipoUsuario.ADMIN);
//                usuarioRepository.save(admin);
//            }
//        };
//    }
//}