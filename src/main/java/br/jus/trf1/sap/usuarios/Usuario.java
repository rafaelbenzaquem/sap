package br.jus.trf1.sap.usuarios;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;


@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private double id;

    private String nome;

    private String chave;

    private short papel;

    private String cpf;

    public Usuario() {
    }

    public Usuario(String nome, String chave, short papel, String cpf) {
        this.nome = nome;
        this.chave = chave;
        this.papel = papel;
        this.cpf = cpf;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public short getPapel() {
        return papel;
    }

    public void setPapel(short papel) {
        this.papel = papel;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Double.compare(id, usuario.id) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", papel=" + papel +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
