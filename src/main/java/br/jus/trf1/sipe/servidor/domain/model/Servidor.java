package br.jus.trf1.sipe.servidor.domain.model;

import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
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
    @JoinColumn(name = "servidor_gestor_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "fk_gestor_servidor"))
    private Servidor gestor;

    @ManyToOne
    @JoinColumn(name = "servidor_gestor_substituto_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "fk_gestor_substituto_servidor"))
    private Servidor gestorSubstituto;

    @ManyToOne
    @JoinColumn(name = "lotacao_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "fk_lotacao_servidor"))
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