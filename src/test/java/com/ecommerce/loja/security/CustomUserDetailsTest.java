package com.ecommerce.loja.security;

import com.ecommerce.loja.model.TipoUsuario;
import com.ecommerce.loja.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void testGetUsername() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@teste.com");

        CustomUserDetails cud = new CustomUserDetails(usuario);

        assertEquals("teste@teste.com", cud.getUsername());
    }

    @Test
    void testGetPassword() {
        Usuario usuario = new Usuario();
        usuario.setSenha("123456");

        CustomUserDetails cud = new CustomUserDetails(usuario);

        assertEquals("123456", cud.getPassword());
    }

    @Test
    void testGetNome() {
        Usuario usuario = new Usuario();
        usuario.setNome("Theo");

        CustomUserDetails cud = new CustomUserDetails(usuario);

        assertEquals("Theo", cud.getNome());
    }

    @Test
    void testGetAuthoritiesQuandoTipoNaoEhNulo() {
        Usuario usuario = new Usuario();
        usuario.setTipo(TipoUsuario.ADMIN);

        CustomUserDetails cud = new CustomUserDetails(usuario);

        Collection<? extends GrantedAuthority> authorities = cud.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetAuthoritiesQuandoTipoEhNulo() {
        Usuario usuario = new Usuario();
        usuario.setTipo(null);

        CustomUserDetails cud = new CustomUserDetails(usuario);

        assertNull(cud.getAuthorities());
    }

    @Test
    void testAccountStatusMethods() {
        Usuario usuario = new Usuario();
        CustomUserDetails cud = new CustomUserDetails(usuario);

        assertTrue(cud.isAccountNonExpired());
        assertTrue(cud.isAccountNonLocked());
        assertTrue(cud.isCredentialsNonExpired());
        assertTrue(cud.isEnabled());
    }

    @Test
    void testGetUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Alexandra");

        CustomUserDetails cud = new CustomUserDetails(usuario);

        assertEquals(usuario, cud.getUsuario());
    }
}
