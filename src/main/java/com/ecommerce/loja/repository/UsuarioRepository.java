package com.ecommerce.loja.repository;

import com.ecommerce.loja.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("all")
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Usuario findByEmail(String email);
    Usuario findByCpf(String cpf);
}