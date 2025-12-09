package br.jus.trf1.sipe.servidor.infrastructure.persistence;

import br.jus.trf1.sipe.lotacao.Lotacao;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "servidores", schema = "sispontodb")
public class ServidorJpa extends UsuarioJpa {

    private String email;

    private String funcao;

    private String cargo;

    @ManyToOne
    @JoinColumn(name = "servidor_gestor_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "fk_gestor_servidor"))
    private ServidorJpa gestor;

    @ManyToOne
    @JoinColumn(name = "servidor_gestor_substituto_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "fk_gestor_substituto_servidor"))
    private ServidorJpa gestorSubstituto;


    @ManyToOne
    @JoinColumn(name = "lotacao_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "fk_lotacao_servidor"))
    private Lotacao lotacao;

    public ServidorJpa(UsuarioJpa usuarioJPA, String email, String funcao, String cargo, Lotacao lotacao) {
        super(usuarioJPA.getId(), usuarioJPA.getNome(), usuarioJPA.getMatricula(), usuarioJPA.getCracha(), usuarioJPA.getHoraDiaria());
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
