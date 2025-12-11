package br.jus.trf1.sipe.servidor.domain.model;

import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Servidor extends Usuario {

    private String email;
    private String funcao;
    private String cargo;

    private Servidor gestor;

    private Servidor gestorSubstituto;

    private LotacaoJpa lotacao;

    public Servidor(Usuario usuario, String email, String funcao, String cargo, LotacaoJpa lotacao) {
        super(usuario.getId(), usuario.getNome(), usuario.getMatricula(), usuario.getCracha(), usuario.getHoraDiaria());
        this.email = email;
        this.funcao = funcao;
        this.cargo = cargo;
        this.lotacao = lotacao;
    }

    @Override
    public String toString() {
        return "Servidor{" +
                "'" + super.toString() + '\'' +
                ", funcao='" + funcao + '\'' +
                ", cargo='" + cargo + '\'' +
                ", siglaLotacao='" + (lotacao != null ? lotacao.getSigla() : "N/A") + "'}";
    }
}