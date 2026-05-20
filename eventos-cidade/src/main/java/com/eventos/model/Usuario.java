package com.eventos.model;

import java.util.Objects;

/**
 * Representa o usuário do sistema de eventos.
 */
public class Usuario {

    private final String nome;
    private final String email;
    private final String cpf;
    private final String telefone;
    private final String cidade;

    public Usuario(String nome, String email, String cpf, String telefone, String cidade) {
        this.nome = Objects.requireNonNull(nome, "Nome é obrigatório").trim();
        this.email = Objects.requireNonNull(email, "E-mail é obrigatório").trim().toLowerCase();
        this.cpf = Objects.requireNonNull(cpf, "CPF é obrigatório").trim();
        this.telefone = Objects.requireNonNull(telefone, "Telefone é obrigatório").trim();
        this.cidade = Objects.requireNonNull(cidade, "Cidade é obrigatória").trim();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCidade() {
        return cidade;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Usuario)) {
            return false;
        }
        Usuario outro = (Usuario) obj;
        return email.equals(outro.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return String.format(
                "Usuário: %s | E-mail: %s | CPF: %s | Telefone: %s | Cidade: %s",
                nome, email, cpf, telefone, cidade
        );
    }
}
