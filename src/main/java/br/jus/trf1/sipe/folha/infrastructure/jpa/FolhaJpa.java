package br.jus.trf1.sipe.folha.infrastructure.jpa;

import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.servidor.infrastructure.jpa.ServidorJpa;
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
public class FolhaJpa {

    @EmbeddedId
    private FolhaJpaId id;

    @CreationTimestamp
    private Timestamp dataAbertura;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor_homologador", referencedColumnName = "id"),
    }, foreignKey = @ForeignKey(name = "fk_homologador_folha"))
    private ServidorJpa servidorHomologador;

    private Timestamp dataHomologacao;

    @OneToMany(mappedBy = "folha", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PontoJpa> pontos;
}
