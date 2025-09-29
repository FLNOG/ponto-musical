package com.ecommerce.loja.service;

import com.ecommerce.loja.model.Usuario;
import com.ecommerce.loja.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@SuppressWarnings("all")
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String codificaSenha(String senha) throws Exception {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public boolean validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito > 9) {
            primeiroDigito = 0;
        }

        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito > 9) {
            segundoDigito = 0;
        }

        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    public Usuario findById(int id) throws Exception {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario cadastro(Usuario usuario) throws Exception {
        if(usuarioRepository.findByEmail(usuario.getEmail())!=null){
            throw new RuntimeException("Email já cadastrado!");
        }

        if (!validarCPF(usuario.getCpf())) {
            throw new RuntimeException("CPF inválido!");
        }

        if (usuarioRepository.findByCpf(usuario.getCpf()) != null) {
            throw new RuntimeException("CPF já cadastrado!");
        }

        usuario.setCpf(usuario.getCpf().replaceAll("[^0-9]", ""));
        usuario.setSenha(codificaSenha(usuario.getSenha()));

        if (usuario.getConfirmarSenha() != null && !usuario.getConfirmarSenha().trim().isEmpty()) {
            usuario.setConfirmarSenha(codificaSenha(usuario.getConfirmarSenha()));
        }

        System.out.println("Senha: " + usuario.getSenha());
        System.out.println("ConfirmarSenha: " + usuario.getConfirmarSenha());

        return usuarioRepository.save(usuario);
    }

    public Usuario editar(int id, Usuario usuarioAtualizado) throws Exception {
        Usuario usuarioExistente = findById(id);

        Usuario usuarioComEmail = usuarioRepository.findByEmail(usuarioAtualizado.getEmail());
        if (usuarioComEmail != null && usuarioComEmail.getId() != id) {
            throw new RuntimeException("Email já está sendo usado por outro usuário!");
        }

        if (!validarCPF(usuarioAtualizado.getCpf())) {
            throw new RuntimeException("CPF inválido!");
        }

        Usuario usuarioComCpf = usuarioRepository.findByCpf(usuarioAtualizado.getCpf().replaceAll("[^0-9]", ""));
        if (usuarioComCpf != null && usuarioComCpf.getId() != id) {
            throw new RuntimeException("CPF já está sendo usado por outro usuário!");
        }

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setCpf(usuarioAtualizado.getCpf().replaceAll("[^0-9]", ""));
        usuarioExistente.setPerfil(usuarioAtualizado.getPerfil());

        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().trim().isEmpty()) {
            usuarioExistente.setSenha(codificaSenha(usuarioAtualizado.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void delete(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }

}