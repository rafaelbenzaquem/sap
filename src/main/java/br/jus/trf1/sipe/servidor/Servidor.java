package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.lotacao.Lotacao;
import br.jus.trf1.sipe.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "servidores", schema = "sispontodb")
public class Servidor extends Usuario {

    private String email;

    private String funcao;

    private String cargo;

    @ManyToOne
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
                ", siglaLotacao='" + lotacao.getSigla() + "'}";
    }
}
