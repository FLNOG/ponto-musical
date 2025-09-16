package com.ecommerce.loja.model;

import jakarta.persistence.*;

@SuppressWarnings("all")
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String senha;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    public enum Perfil {
        CLIENTE,
        ADMINISTRADOR
    }

    @Transient
    private String confirmarSenha;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getConfirmarSenha() { return confirmarSenha; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
}