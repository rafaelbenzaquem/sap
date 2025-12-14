package br.jus.trf1.sipe.servidor.domain.model;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private Lotacao lotacao;

    public Servidor(Usuario usuario, String email, String funcao, String cargo, Lotacao lotacao) {
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