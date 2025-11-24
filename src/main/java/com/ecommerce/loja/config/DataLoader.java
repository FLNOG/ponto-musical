package com.ecommerce.loja.config;

import com.ecommerce.loja.model.Produto;
import com.ecommerce.loja.model.TipoUsuario;
import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.service.ProdutoService;
import com.ecommerce.loja.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner carregarUsuarios(UsuarioService usuarioService) {
        return args -> {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            // Usuário administrador
            if (!usuarioService.cpfExiste("00000000000")) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@loja.com");
                admin.setCpf("00000000000");
                admin.setTipo(TipoUsuario.ADMIN);
                admin.setSenha(encoder.encode("123")); // senha criptografada
                usuarioService.salvar(admin);
            }

            // Usuário cliente
            if (!usuarioService.cpfExiste("11111111111")) {
                Usuario cliente = new Usuario();
                cliente.setNome("Cliente Teste");
                cliente.setEmail("cliente@loja.com");
                cliente.setCpf("11111111111");
                cliente.setTipo(TipoUsuario.CLIENTE);
                cliente.setSenha(encoder.encode("123")); // senha criptografada
                usuarioService.salvar(cliente);
            }

        };
    }

    @Bean
    public CommandLineRunner carregarProdutos(ProdutoService produtoService) {
        return args -> {

            if (produtoService.listarTodos().isEmpty()) {

                Produto p1 = new Produto("Guitarra Fender Stratocaster", 4500.00, 5, "Guitarra elétrica clássica");
                p1.setImagem("");
                produtoService.salvar(p1);

                Produto p2 = new Produto("Bateria Pearl Export", 3200.00, 2, "Bateria completa para iniciantes");
                p2.setImagem("");
                produtoService.salvar(p2);

                Produto p3 = new Produto("Teclado Yamaha PSR", 1800.00, 7, "Teclado eletrônico portátil");
                p3.setImagem("");
                produtoService.salvar(p3);
            }
        };
    }
}