package br.jus.trf1.sipe.lotacao.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "sisponto", name = "lotacoes")
public class LotacaoJpa {

    @Id
    private Integer id;

    private String sigla;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_lotacao_pai")
    private LotacaoJpa lotacaoPai;

    @OneToMany(mappedBy = "lotacaoPai", fetch = FetchType.LAZY)
    private List<LotacaoJpa> subLotacoes;
}
