package com.ecommerce.loja.repository;

import com.ecommerce.loja.model.TipoUsuario;
import com.ecommerce.loja.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByEmailExists() {
        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setEmail("joao@gmail.com");
        usuario.setSenha("123");
        usuario.setCpf("11111111111");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuarioRepository.save(usuario);

        Optional<Usuario> result = usuarioRepository.findByEmail("joao@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("João", result.get().getNome());
    }

    @Test
    void testFindByEmailNotExists() {
        Optional<Usuario> result = usuarioRepository.findByEmail("naoexiste@gmail.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByCpfExists() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setEmail("maria@gmail.com");
        usuario.setSenha("123");
        usuario.setCpf("22222222222");
        usuario.setTipo(TipoUsuario.ADMIN);
        usuarioRepository.save(usuario);

        Optional<Usuario> result = usuarioRepository.findByCpf("22222222222");

        assertTrue(result.isPresent());
        assertEquals("Maria", result.get().getNome());
    }

    @Test
    void testFindByCpfNotExists() {
        Optional<Usuario> result = usuarioRepository.findByCpf("99999999999");
        assertTrue(result.isEmpty());
    }
}
