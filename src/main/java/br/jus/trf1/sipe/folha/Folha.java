package br.jus.trf1.sipe.folha;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.servidor.infrastructure.persistence.ServidorJpa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "folhas", schema = "sispontodb")
public class Folha {

    @EmbeddedId
    private FolhaId id;

    @CreationTimestamp
    private Timestamp dataAbertura;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor_homologador", referencedColumnName = "id"),
    }, foreignKey = @ForeignKey(name = "fk_homologador_folha"))
    private ServidorJpa servidorHomologador;

    private Timestamp dataHomologacao;

    @OneToMany(mappedBy = "folha", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ponto> pontos;


    public void homologar(ServidorJpa servidor) {
        if(servidor.getFuncao().contains("DIRETOR")){
            this.servidorHomologador = servidor;
            this.dataHomologacao = new Timestamp(System.currentTimeMillis());
        }
        throw new IllegalArgumentException("Somente Diretor pode homologar uma folha!");
    }
}
