package com.ecommerce.loja.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123456");
        usuario.setCpf("11122233344");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setTelefone("11999999999");
        usuario.setDataNascimento("2000-01-01");

        assertEquals(1, usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("123456", usuario.getSenha());
        assertEquals("11122233344", usuario.getCpf());
        assertEquals(TipoUsuario.CLIENTE, usuario.getTipo());
        assertEquals("11999999999", usuario.getTelefone());
        assertEquals("2000-01-01", usuario.getDataNascimento());
    }

    @Test
    void testUsuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setEmail("maria@gmail.com");
        usuario.setSenha("senha123");
        usuario.setCpf("12345678900");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNomeObrigatorio() {
        Usuario usuario = new Usuario();
        usuario.setNome("");
        usuario.setEmail("teste@gmail.com");
        usuario.setSenha("123");
        usuario.setCpf("11111111111");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome é obrigatório")));
    }

    @Test
    void testEmailInvalido() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("email_invalido");
        usuario.setSenha("123");
        usuario.setCpf("11111111111");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("E-mail inválido")));
    }

    @Test
    void testEmailObrigatorio() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("");
        usuario.setSenha("123");
        usuario.setCpf("11111111111");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O e-mail é obrigatório")));
    }

    @Test
    void testSenhaObrigatoria() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@gmail.com");
        usuario.setSenha("");
        usuario.setCpf("11111111111");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A senha é obrigatória")));
    }

    @Test
    void testCpfObrigatorio() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@gmail.com");
        usuario.setSenha("123");
        usuario.setCpf("");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O CPF é obrigatório")));
    }

    @Test
    void testTipoUsuarioEnum() {
        Usuario usuario = new Usuario();
        usuario.setTipo(TipoUsuario.ADMIN);
        assertEquals(TipoUsuario.ADMIN, usuario.getTipo());
    }
}