package com.ecommerce.loja.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TipoUsuarioTest {

    @Test
    void testEnumValues() {
        TipoUsuario[] values = TipoUsuario.values();
        assertEquals(2, values.length);
        assertEquals(TipoUsuario.ADMIN, values[0]);
        assertEquals(TipoUsuario.CLIENTE, values[1]);
    }

    @Test
    void testDescricaoAdmin() {
        assertEquals("Administrador", TipoUsuario.ADMIN.getDescricao());
    }

    @Test
    void testDescricaoCliente() {
        assertEquals("Cliente", TipoUsuario.CLIENTE.getDescricao());
    }

    @Test
    void testValueOf() {
        assertEquals(TipoUsuario.ADMIN, TipoUsuario.valueOf("ADMIN"));
        assertEquals(TipoUsuario.CLIENTE, TipoUsuario.valueOf("CLIENTE"));
    }

    @Test
    void testDescricaoNaoNula() {
        for (TipoUsuario tipo : TipoUsuario.values()) {
            assertNotNull(tipo.getDescricao());
            assertFalse(tipo.getDescricao().isBlank());
        }
    }
}

