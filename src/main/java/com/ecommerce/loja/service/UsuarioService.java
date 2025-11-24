package com.ecommerce.loja.service;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos os usuários
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar usuário por ID
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    // Salvar ou atualizar usuário
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Deletar usuário por ID
    public void deletar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Buscar usuário pelo e-mail
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Buscar se cpf existe
    public boolean cpfExiste(String cpf) {
        return usuarioRepository.findByCpf(cpf).isPresent();
    }
}