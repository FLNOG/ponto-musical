package com.ecommerce.loja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth
                        // 1. Permite acesso a recursos estáticos (CSS, JS, Imagens, etc.)
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login/**", "/webjars/**").permitAll()

                        // 2. REQUER AUTENTICAÇÃO para qualquer rota que comece com /admin/**
                        .requestMatchers("/admin/**").permitAll()

                        // 3. Todas as outras rotas (incluindo /, /login, /home) são permitidas (públicas)
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")               // URL para exibir o formulário de login customizado
                        .failureUrl("/login?error")        // URL em caso de falha no login
                        // Redireciona para a home após login (Se não houver URL de origem armazenada)
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")              // URL que o formulário de logout fará POST
                        .logoutSuccessUrl("/login?logout") // Redireciona após logout bem-sucedido
                )
                // Desativa CSRF para simplificar o desenvolvimento. Mantenha em produção se usar formulários.
                .csrf(AbstractHttpConfigurer::disable)

                // Habilita a renderização de wireframe (necessário para o H2DB)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usa BCrypt, que é o padrão seguro para hashear senhas.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService users(PasswordEncoder passwordEncoder) {

        // Detalhes do usuário de teste
        org.springframework.security.core.userdetails.UserDetails user =
                org.springframework.security.core.userdetails.User.builder()
                        .username("admin")
                        // A senha 'admin' será codificada pelo BCryptPasswordEncoder
                        .password(passwordEncoder.encode("admin"))
                        .roles("USER", "ADMIN") // Roles para uso futuro em autorização
                        .build();

        // Gerenciador em memória (apenas para testes)
        return new org.springframework.security.provisioning.InMemoryUserDetailsManager(user);

    }
}